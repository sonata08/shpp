package com.example.myprofile.data.network.dto

import com.example.myprofile.data.model.User

data class LoginResponse (
    val status: String,
    val code: String,
    val message: String?,
    val data: LoginResponseData
)

data class LoginResponseData(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)



