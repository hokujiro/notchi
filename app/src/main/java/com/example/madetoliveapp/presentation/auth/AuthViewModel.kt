package com.example.madetoliveapp.presentation.auth

import android.app.Application
import android.content.Context
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

    fun loginWithGoogle(idToken: String, context: Context) {
        viewModelScope.launch {
                // Step 1: Create a request to send the token to your backend
            val response = googleAuthUseCase(GoogleAuthRequest(idToken))
            response.fold(
                onSuccess = {
                    // ✅ Store JWT token returned by backend
                    val jwt = it.token
                    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("jwt_token", jwt).apply()
                },
                onFailure = {
                    // Handle failure
                }
            )
        }
    }
}