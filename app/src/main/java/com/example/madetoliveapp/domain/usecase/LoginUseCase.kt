package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = authRepository.login(authRequest).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

