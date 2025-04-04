package com.example.madetoliveapp.data.source.remote.auth

import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // Endpoint for user login
    @POST("api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    // Endpoint for user registration
    @POST("api/auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse> // Void response for registration

    @POST("auth/google-login")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): Response<GoogleAuthResponse>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<TokenResponse>
}