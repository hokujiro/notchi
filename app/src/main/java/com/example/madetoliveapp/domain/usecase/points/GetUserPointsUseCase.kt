package com.example.madetoliveapp.domain.usecase.points

import com.example.madetoliveapp.data.repository.user.UserRepository

class GetUserPointsUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Float {
        return userRepository.getTotalPoints()
    }
}