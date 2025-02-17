package com.example.madetoliveapp.presentation.auth


import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import android.os.Bundle
import android.util.Log
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
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.madetoliveapp.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import org.koin.androidx.compose.koinViewModel
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.util.Collections


class AuthActivity : AppCompatActivity() {

    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreen()
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
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        GoogleSignInUtils.doGoogleSignIn(
            context = context,
            scope = scope,
            launcher = null,
            login = {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            },
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

        if (!isLoginMode) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (isLoginMode) {
                    viewModel.login(username, password)
                } else {
                    viewModel.register(username, password)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
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
                ) },
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
}

private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, viewModel: AuthViewModel) {
    try {
        val account = task.getResult(ApiException::class.java)
        account?.idToken?.let { token ->
            viewModel.loginWithGoogle(token)
        }
    } catch (e: ApiException) {
        e.printStackTrace()
    }
}


fun verifyGoogleIdToken(idTokenString: String, clientId: String) {
    val transport = NetHttpTransport()
    val jsonFactory = JacksonFactory.getDefaultInstance()

    val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(clientId)) // Set your CLIENT_ID
        .build()

    try {
        val idToken: GoogleIdToken? = verifier.verify(idTokenString)

        if (idToken != null) {
            val payload: Payload = idToken.payload

            // Extract user details
            val userId = payload.subject
            val email = payload.email
            val emailVerified = payload.emailVerified ?: false
            val name = payload["name"] as? String
            val pictureUrl = payload["picture"] as? String
            val locale = payload["locale"] as? String
            val familyName = payload["family_name"] as? String
            val givenName = payload["given_name"] as? String

            // Print user details
            println("User ID: $userId")
            println("Email: $email (Verified: $emailVerified)")
            println("Name: $name")
            println("Picture URL: $pictureUrl")
            println("Locale: $locale")
            println("Family Name: $familyName")
            println("Given Name: $givenName")

            // Use or store profile information
        } else {
            println("Invalid ID token.")
        }
    } catch (e: Exception) {
        println("Token verification failed: ${e.message}")
    }
}
