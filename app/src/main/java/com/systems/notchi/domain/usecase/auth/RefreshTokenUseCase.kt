package com.systems.notchi.domain.usecase.auth

import com.systems.notchi.data.repository.auth.AuthRepository
import com.systems.notchi.data.source.remote.auth.TokenResponse
import retrofit2.Response

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(refreshToken: String): Response<TokenResponse> {
        return authRepository.refreshToken(refreshToken)
    }
}