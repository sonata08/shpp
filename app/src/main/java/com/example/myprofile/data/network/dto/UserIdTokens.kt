package com.example.myprofile.data.network.dto

data class UserIdTokens(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String
)
