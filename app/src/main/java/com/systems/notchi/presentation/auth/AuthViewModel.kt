package com.systems.notchi.presentation.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.notchi.data.source.remote.auth.AuthRequest
import com.systems.notchi.data.source.remote.googleauth.GoogleAuthRequest
import com.systems.notchi.domain.usecase.auth.GoogleAuthUseCase
import com.systems.notchi.domain.usecase.auth.LoginUseCase
import com.systems.notchi.domain.usecase.auth.RefreshTokenUseCase
import com.systems.notchi.domain.usecase.auth.RegisterUseCase
import com.systems.notchi.presentation.tasks.TasksActivity
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
            val response = googleAuthUseCase(GoogleAuthRequest(idToken))
            response.fold(
                onSuccess = {
                    Log.d("AuthViewModel", "Google login success. Saving tokens.")
                    tokenManager.saveTokens(it.token, it.refreshToken)

                    _navigateToHome.value = true
                    val intent = Intent(context, TasksActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)

                },
                onFailure = {
                    Log.e("AuthViewModel", "Google login failed: ${it.message}", it)
                    uiState = it.message ?: "Google login failed"
                    Toast.makeText(context, "Google login failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun checkSessionAndNavigate() {
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        when {
            !accessToken.isNullOrEmpty() -> {
                _navigationTarget.value = NavigationTarget.TASKS
            }

            !refreshToken.isNullOrEmpty() -> {
                viewModelScope.launch {
                    try {
                        val response = refreshTokenUseCase(refreshToken) // Now returns Response<TokenResponse>
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!
                            tokenManager.saveTokens(body.accessToken, body.refreshToken)
                            _navigationTarget.value = NavigationTarget.TASKS
                        } else {
                            tokenManager.clearTokens()
                            _navigationTarget.value = NavigationTarget.AUTH
                        }
                    } catch (e: Exception) {
                        tokenManager.clearTokens()
                        _navigationTarget.value = NavigationTarget.AUTH
                    }
                }
            }

            else -> {
                _navigationTarget.value = NavigationTarget.AUTH
            }
        }
    }

    enum class NavigationTarget {
        TASKS,
        AUTH
    }
}