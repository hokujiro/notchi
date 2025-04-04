package com.example.madetoliveapp.data.source.remote.auth

data class RefreshTokenRequest(
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)