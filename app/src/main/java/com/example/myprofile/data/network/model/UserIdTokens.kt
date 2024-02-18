package com.example.myprofile.data.network.model

data class UserIdTokens(
    val userId: Long = -1,
    val accessToken: String = "",
    val refreshToken: String = ""
)
