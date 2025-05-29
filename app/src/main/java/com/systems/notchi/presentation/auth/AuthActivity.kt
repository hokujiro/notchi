package com.systems.notchi.presentation.auth


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.systems.notchi.presentation.rewards.RewardsActivity
import com.systems.notchi.presentation.theme.MadeToLiveTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.systems.notchi.R
import com.systems.notchi.presentation.tasks.TasksActivity


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                AuthScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(viewModel: AuthViewModel = koinViewModel()) {
    var isLoginMode by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val shouldNavigate by viewModel.navigateToHome.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        GoogleSignInUtils.handleSignInResultFromIntent(
            context = context,
            data = result.data,
            onTokenReceived = { token ->
                viewModel.loginWithGoogle(token, context)
            },
            onFailure = { error ->
                Toast.makeText(
                    context,
                    "Google Sign-In failed: ${error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    // ✅ Auto-login with Google if credentials exist
    val attemptedAutoLogin = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(attemptedAutoLogin.value) {
        if (!attemptedAutoLogin.value && GoogleSignInUtils.hasGoogleAccount(context)) {
            attemptedAutoLogin.value = true

            GoogleSignInUtils.doGoogleSignIn(
                context = context,
                scope = scope,
                launcher = launcher,
                onTokenReceived = { token ->
                    viewModel.loginWithGoogle(token, context)
                },
                onFailure = { e ->
                    Toast.makeText(context, "Google sign-in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.notchi_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(240.dp)
                    .padding(bottom = 24.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.White) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    containerColor = Color(0xFF121212),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    containerColor = Color(0xFF121212),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp)
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
                        onTokenReceived = { token ->
                            viewModel.loginWithGoogle(token, context)
                        },
                        onFailure = { error ->
                            Toast.makeText(context, "Google sign-in failed: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
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
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        // ✅ Show error or success message
        LaunchedEffect(viewModel.uiState) {
            viewModel.uiState?.let { state ->
                message = state
            }
        }

        // ✅ Navigate on successful login
        LaunchedEffect(shouldNavigate) {
            if (shouldNavigate) {
                val intent = Intent(context, TasksActivity::class.java)
                context.startActivity(intent)
                if (context is Activity) context.finish()
            }
        }
    }
}