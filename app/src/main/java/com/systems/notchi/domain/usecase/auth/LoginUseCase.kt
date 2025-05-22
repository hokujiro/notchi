package com.systems.notchi.domain.usecase.auth

import com.systems.notchi.data.repository.auth.AuthRepository
import com.systems.notchi.data.source.remote.auth.AuthRequest
import com.systems.notchi.data.source.remote.auth.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authRequest: AuthRequest): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authRepository.login(authRequest)
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
}

