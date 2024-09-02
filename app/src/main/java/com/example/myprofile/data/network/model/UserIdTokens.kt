package com.example.myprofile.data.network.model

import com.example.myprofile.data.network.INVALID_ID

data class UserIdTokens(
    val userId: Long = INVALID_ID,
    val accessToken: String = "",
    val refreshToken: String = ""
)
