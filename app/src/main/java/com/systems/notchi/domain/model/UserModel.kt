package com.systems.notchi.domain.model

data class UserModel(
    val id: Long,
    val username: String,
    val email: String,
    val photo: String?,
    val totalPoints: Float
)