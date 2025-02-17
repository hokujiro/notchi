package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthResponse
import com.example.madetoliveapp.domain.model.TaskModel
import retrofit2.Call
import retrofit2.Response

interface AuthRepository {
    suspend fun login(authRequest: AuthRequest): Call<AuthResponse> // Fetch all tasks
    suspend fun register(authRequest: AuthRequest): Call<Void> // Fetch all tasks
    suspend fun googleSignIn(authRequest: GoogleAuthRequest): Response<GoogleAuthResponse>  // Fetch all tasks
}
