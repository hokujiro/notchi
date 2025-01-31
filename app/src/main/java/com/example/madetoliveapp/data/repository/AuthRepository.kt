package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse
import com.example.madetoliveapp.domain.model.TaskModel
import retrofit2.Call

interface AuthRepository {
    suspend fun login(authRequest: AuthRequest): Call<AuthResponse> // Fetch all tasks
    suspend fun register(authRequest: AuthRequest): Call<Void> // Fetch all tasks
}
