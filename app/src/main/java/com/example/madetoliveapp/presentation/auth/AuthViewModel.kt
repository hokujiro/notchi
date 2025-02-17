package com.example.madetoliveapp.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.domain.usecase.GoogleAuthUseCase
import com.example.madetoliveapp.domain.usecase.LoginUseCase
import com.example.madetoliveapp.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase
) : ViewModel() {

    var uiState by mutableStateOf<String?>(null)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase(AuthRequest(username, password))
            uiState = result.fold(
                onSuccess = { "Login successful: ${it.token}" },
                onFailure = { it.message ?: "An error occurred during login" }
            )
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val result = registerUseCase(AuthRequest(username, password))
            uiState = result.fold(
                onSuccess = { "Registration successful" },
                onFailure = { it.message ?: "An error occurred during registration" }
            )
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
                // Step 1: Create a request to send the token to your backend
            val response =  googleAuthUseCase(GoogleAuthRequest(idToken))
            uiState = response.fold(
                onSuccess = { "Login successful: ${it.token}" },
                onFailure = { it.message ?: "An error occurred during login" }
            )
            response.onSuccess {
                // Store authentication token for session management
                saveAuthToken(it.token)
            }
        }
    }

    // Save the authentication token in SharedPreferences or DataStore
    private fun saveAuthToken(token: String) {
        // Implementation to securely store the token
    }
}