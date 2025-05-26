package com.systems.notchi.data.repository.user

import com.systems.notchi.domain.model.UserModel
import okhttp3.MultipartBody

interface UserRepository {
    suspend fun getTotalPoints(): Float
    suspend fun getCurrentUser(): UserModel
    suspend fun updateUserProfile(username: String)
    suspend fun uploadProfilePhoto(filePart: MultipartBody.Part): String
}