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
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

    // Endpoint for user registration
    @POST("api/auth/register")
    fun register(@Body authRequest: AuthRequest): Call<Void> // Void response for registration

    @POST("auth/google-login")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): Response<GoogleAuthResponse>
}