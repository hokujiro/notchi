package com.systems.notchi.data.repository.auth

import com.systems.notchi.data.source.remote.auth.AuthRequest
import com.systems.notchi.data.source.remote.auth.AuthResponse
import com.systems.notchi.data.source.remote.auth.TokenResponse
import com.systems.notchi.data.source.remote.googleauth.GoogleAuthRequest
import retrofit2.Response

interface AuthRepository {
    suspend fun login(authRequest: AuthRequest): Response<AuthResponse> // Fetch all tasks
    suspend fun register(authRequest: AuthRequest): Response<AuthResponse> // Fetch all tasks
    suspend fun googleSignIn(authRequest: GoogleAuthRequest): Response<AuthResponse>  // Fetch all tasks
    suspend fun refreshToken(refreshToken: String): Response<TokenResponse>
}
