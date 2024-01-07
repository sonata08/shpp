package com.example.myprofile.data.network.model

import com.example.myprofile.data.model.User

data class LoginResponseBase (
    val status: String,
    val code: String,
    val message: String?,
    val data: LoginResponse
)

data class LoginResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)



