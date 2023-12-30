package com.example.myprofile.data.network.dto

data class UserIdTokens(
    val userId: Long = -1,
    val accessToken: String = "",
    val refreshToken: String = ""
)
