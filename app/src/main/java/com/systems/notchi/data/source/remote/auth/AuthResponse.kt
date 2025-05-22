package com.systems.notchi.data.source.remote.auth

data class AuthResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val token: String,         // access token
    val refreshToken: String
)