package com.example.madetoliveapp.data.repository.user

interface UserRepository {
    suspend fun getTotalPoints(): Float
}