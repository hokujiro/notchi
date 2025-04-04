package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.domain.model.TaskModel

interface UserRepository {
    suspend fun getTotalPoints(): Float
}