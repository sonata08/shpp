package com.example.myprofile.data.network.dto

import kotlinx.serialization.Serializable

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)


data class RefreshTokenFull(
    val status: String,
    val code: String,
    val message: String?,
    val data: Tokens
)