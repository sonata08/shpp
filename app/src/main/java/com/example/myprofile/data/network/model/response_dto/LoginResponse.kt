package com.example.myprofile.data.network.model.response_dto

import com.example.myprofile.data.model.User

data class LoginResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)
