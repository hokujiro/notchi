package com.systems.notchi.domain.usecase.auth

import com.systems.notchi.data.repository.auth.AuthRepository
import com.systems.notchi.data.source.remote.auth.AuthResponse
import com.systems.notchi.data.source.remote.googleauth.GoogleAuthRequest

class GoogleAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(googleAuthRequest: GoogleAuthRequest): Result<AuthResponse> {
        return try {
            val response = authRepository.googleSignIn(googleAuthRequest)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
