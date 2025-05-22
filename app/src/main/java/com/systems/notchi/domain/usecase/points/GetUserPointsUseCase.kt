package com.systems.notchi.domain.usecase.points

import com.systems.notchi.data.repository.user.UserRepository

class GetUserPointsUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Float {
        return userRepository.getTotalPoints()
    }
}