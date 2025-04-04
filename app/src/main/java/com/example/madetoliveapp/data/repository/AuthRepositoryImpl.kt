package com.example.madetoliveapp.data.repository


import com.example.madetoliveapp.data.source.remote.auth.AuthApi
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse
import com.example.madetoliveapp.data.source.remote.auth.RefreshTokenRequest
import com.example.madetoliveapp.data.source.remote.auth.TokenResponse
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthResponse
import retrofit2.Call
import retrofit2.Response

class AuthRepositoryImpl(
    private val authApi: AuthApi,
) : AuthRepository {

    // Fetch all tasks from the database
    override suspend fun login(authRequest: AuthRequest): Response<AuthResponse> {
        return authApi.login(authRequest)
    }

    // Fetch all tasks from the database
    override suspend fun register(authRequest: AuthRequest): Response<AuthResponse> {
        return authApi.register(authRequest)
    }

    override suspend fun googleSignIn(authRequest: GoogleAuthRequest): Response<GoogleAuthResponse> {
        return authApi.loginWithGoogle(authRequest)
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenResponse> {
        return try {
            val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to refresh token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}