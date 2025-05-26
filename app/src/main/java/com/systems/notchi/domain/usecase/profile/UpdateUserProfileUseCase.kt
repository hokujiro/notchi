package com.systems.notchi.domain.usecase.profile

import com.systems.notchi.data.repository.user.UserRepository

class UpdateUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(username: String, profileImageUri: String?) {
        userRepository.updateUserProfile(username)
    }
}