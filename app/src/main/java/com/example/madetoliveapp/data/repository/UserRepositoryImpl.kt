package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.mapper.RemoteMapper
import com.example.madetoliveapp.data.source.remote.api.UserApi

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
}