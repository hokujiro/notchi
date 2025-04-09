package com.example.madetoliveapp.domain.usecase.auth

import com.example.madetoliveapp.data.repository.auth.AuthRepository
import com.example.madetoliveapp.data.source.remote.auth.AuthRequest
import com.example.madetoliveapp.data.source.remote.auth.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authRequest: AuthRequest): Result<AuthResponse> {
        //You're using .execute(), which is a blocking (synchronous) network call.
        //Since this code runs inside your viewModelScope.launch { ... }, and you're not switching to a background thread (Dispatchers.IO)
        //it runs on the main thread
        //This way, even though .execute() blocks, it’ll do so on the IO thread, not the UI thread.
        return withContext(Dispatchers.IO) {
            try {
                val response = authRepository.register(authRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}