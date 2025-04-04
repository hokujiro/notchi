package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.UserRepository

class GetUserPointsUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Float {
        return userRepository.getTotalPoints()
    }
}