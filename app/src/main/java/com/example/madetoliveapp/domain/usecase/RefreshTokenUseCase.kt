package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.source.remote.auth.TokenResponse

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(refreshToken: String): Result<TokenResponse> {
        return authRepository.refreshToken(refreshToken)
    }
}