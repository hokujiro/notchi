package com.example.madetoliveapp.data.source.remote.auth

data class AuthResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val token: String
)