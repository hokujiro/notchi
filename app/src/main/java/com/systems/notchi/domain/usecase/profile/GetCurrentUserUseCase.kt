package com.systems.notchi.domain.usecase.profile

import com.systems.notchi.data.repository.user.UserRepository
import com.systems.notchi.domain.model.UserModel

class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(): UserModel {
        return userRepository.getCurrentUser()
    }
}