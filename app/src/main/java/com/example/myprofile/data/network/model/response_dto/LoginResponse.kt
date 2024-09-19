package com.example.myprofile.data.network.model.response_dto

import androidx.annotation.Keep
import com.example.myprofile.data.model.User

@Keep
data class LoginResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)
