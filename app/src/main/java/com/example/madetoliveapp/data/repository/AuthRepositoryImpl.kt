package com.example.madetoliveapp.data.repository


import com.example.madetoliveapp.data.source.remote.auth.AuthApi
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse
import retrofit2.Call

class AuthRepositoryImpl(
    private val authApi: AuthApi,
) : AuthRepository {

    // Fetch all tasks from the database
    override suspend fun login(authRequest: AuthRequest): Call<AuthResponse> {
        return authApi.login(authRequest)
    }

    // Fetch all tasks from the database
    override suspend fun register(authRequest: AuthRequest): Call<Void> {
        return authApi.register(authRequest)
    }
}