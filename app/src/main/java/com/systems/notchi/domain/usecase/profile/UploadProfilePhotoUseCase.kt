package com.systems.notchi.domain.usecase.profile

import com.systems.notchi.data.repository.user.UserRepository
import okhttp3.MultipartBody

class UploadProfilePhotoUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(filePart: MultipartBody.Part): String {
        return userRepository.uploadProfilePhoto(filePart)
    }
}