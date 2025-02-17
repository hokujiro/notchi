package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthRequest
import com.example.madetoliveapp.data.source.remote.googleauth.GoogleAuthResponse
import retrofit2.Response

class GoogleAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(googleAuthRequest: GoogleAuthRequest): Result<GoogleAuthResponse> {
        return try {
            val response = authRepository.googleSignIn(googleAuthRequest)

            if (response.isSuccessful) { // Ensure the request was successful
                response.body()?.let {
                    Result.success(it) // Return successful response body
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
