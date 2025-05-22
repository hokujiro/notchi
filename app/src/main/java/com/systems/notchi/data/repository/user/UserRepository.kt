package com.systems.notchi.data.repository.user

interface UserRepository {
    suspend fun getTotalPoints(): Float
}