package com.systems.notchi.data.repository.user

import com.systems.notchi.data.mapper.RemoteMapper
import com.systems.notchi.data.source.remote.api.UserApi
import com.systems.notchi.domain.model.UserModel
import okhttp3.MultipartBody

class UserRepositoryImpl(
    private val mapper: RemoteMapper,
    private val userApi: UserApi
): UserRepository {
   override suspend fun getTotalPoints(): Float {
        val response = userApi.getUserPoints()
        if (response.isSuccessful) {
            return response.body() ?: 0f
        } else {
            throw Exception("Failed to fetch user points: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getCurrentUser(): UserModel {
        val response = userApi.getCurrentUser()
        if (response.isSuccessful) {
            val dto = response.body() ?: throw Exception("Empty response body")
            return mapper.toUserModel(dto)
        } else {
            throw Exception("Failed to fetch current user: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun updateUserProfile(username: String) {
        val request = UpdateUserRequest(username = username)
        val response = userApi.updateUserProfile(request)
        if (!response.isSuccessful) {
            throw Exception("Failed to update profile: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun uploadProfilePhoto(filePart: MultipartBody.Part): String {
        val response = userApi.uploadProfilePhoto(filePart)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No URL returned")
        } else {
            throw Exception("Upload failed: ${response.errorBody()?.string()}")
        }
    }
}

data class UpdateUserRequest(
    val username: String,
)
