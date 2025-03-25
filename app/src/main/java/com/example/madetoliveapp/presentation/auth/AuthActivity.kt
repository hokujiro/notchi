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
import com.example.madetoliveapp.presentation.home.HomeActivity
import org.koin.androidx.compose.koinViewModel


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
    val onLoginSuccess = {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, HomeActivity::class.java))
        if (context is Activity) context.finish()
    }
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

        if (!isLoginMode) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

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
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope = scope,
                    launcher = launcher,
                    login = onLoginSuccess,
                    viewModel = viewModel
                )
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
}

