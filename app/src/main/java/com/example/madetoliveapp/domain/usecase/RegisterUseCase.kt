package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authRequest: AuthRequest): Result<Unit> {
        return try {
            val response = authRepository.register(authRequest).execute()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}