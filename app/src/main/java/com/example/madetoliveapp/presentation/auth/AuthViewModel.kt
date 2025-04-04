package com.example.madetoliveapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.RefreshTokenRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.domain.usecase.GoogleAuthUseCase
import com.example.madetoliveapp.domain.usecase.LoginUseCase
import com.example.madetoliveapp.domain.usecase.RefreshTokenUseCase
import com.example.madetoliveapp.domain.usecase.RegisterUseCase
import com.example.madetoliveapp.presentation.rewards.RewardsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase,
    private val tokenManager: TokenManager,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : ViewModel() {

    var uiState by mutableStateOf<String?>(null)
        private set

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _navigationTarget = MutableStateFlow<NavigationTarget?>(null)
    val navigationTarget: StateFlow<NavigationTarget?> = _navigationTarget.asStateFlow()

    init {
        checkSessionAndNavigate()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase(AuthRequest(username, password))
            uiState = result.fold(
                onSuccess = {
                    // ✅ Save both tokens
                    tokenManager.saveTokens(it.token, it.refreshToken)
                    Log.d("TokenManager", "Saved accessToken: ${it.token}")
                    _navigateToHome.value = true
                    "Login successful"
                },
                onFailure = { it.message ?: "An error occurred during login" }
            )
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val result = registerUseCase(AuthRequest(username, password))
            uiState = result.fold(
                onSuccess = {
                    // ✅ Save both tokens
                    tokenManager.saveTokens(it.token, it.refreshToken)
                    _navigateToHome.value = true
                    "Registration successful"
                },
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

                    // ✅ Navigate to HomeActivity
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                },
                onFailure = {
                    // Handle failure
                }
            )
        }
    }

    private fun checkSessionAndNavigate() {
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        if (!accessToken.isNullOrEmpty()) {
            _navigationTarget.value = NavigationTarget.TASKS
        } else if (!refreshToken.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = refreshTokenUseCase(refreshToken)
                result.fold(
                    onSuccess = {
                        tokenManager.saveTokens(it.accessToken, it.refreshToken)
                        _navigationTarget.emit(NavigationTarget.TASKS)
                    },
                    onFailure = {
                        tokenManager.clearTokens()
                        _navigationTarget.emit(NavigationTarget.AUTH)
                    }
                )
            }
        } else {
            _navigationTarget.value = NavigationTarget.AUTH
        }
    }

    enum class NavigationTarget {
        TASKS,
        AUTH
    }
}