package com.example.madetoliveapp.presentation.auth


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.madetoliveapp.BuildConfig
import com.example.madetoliveapp.presentation.rewards.RewardsActivity
import com.example.madetoliveapp.presentation.tasks.TasksActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.koinViewModel


class AuthActivity : AppCompatActivity() {

    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       checkIfGoogleAccountIsSignedIn()
        setContent {
            AuthScreen()
        }
    }

    private fun checkIfGoogleAccountIsSignedIn() {
        val credentialManager = CredentialManager.create(this)
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true) // Only show authorized accounts
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setAutoSelectEnabled(true)          // 👈 Enables auto-selection
                    .build()
            )
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@AuthActivity, request)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val token = googleCredential.idToken

                    // Sign in to Firebase (optional, for Firebase auth)
                    val authCredential = GoogleAuthProvider.getCredential(token, null)
                    val user = Firebase.auth.signInWithCredential(authCredential).await().user

                    if (user != null && !user.isAnonymous) {
                        // Call your ViewModel to finish login and navigate
                        val viewModel: AuthViewModel = ViewModelProvider(this@AuthActivity)[AuthViewModel::class.java]
                        viewModel.loginWithGoogle(token, this@AuthActivity)
                        // You may navigate directly here too if you want to skip ViewModel
                        val intent = Intent(this@AuthActivity, TasksActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }

            } catch (e: NoCredentialException) {
                // No saved account — show login screen
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            }
        }
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel = koinViewModel()) {
    var isLoginMode by remember { mutableStateOf(true) } // Toggle between Login and Register
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val onLoginSuccess = {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, RewardsActivity::class.java))
        if (context is Activity) context.finish()
    }
    val shouldNavigate by viewModel.navigateToHome.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            GoogleSignInUtils.doGoogleSignIn(
                context = context,
                scope = scope,
                launcher = null,
                login = onLoginSuccess,
                viewModel = viewModel
            )
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLoginMode) "Login" else "Register",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (isLoginMode)
                    viewModel.login(username, password)
                else
                viewModel.register(username, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(if (isLoginMode) "Login" else "Register")
        }

        Button(
            onClick = {
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope = scope,
                    launcher = launcher,
                    login = {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    },
                    viewModel = viewModel
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login")
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    LaunchedEffect(viewModel.uiState) {
        viewModel.uiState?.let { state ->
            message = state
        }
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            val intent = Intent(context, RewardsActivity::class.java)
            context.startActivity(intent)
            // You might want to finish the current activity here via callback if needed
        }
    }
}

