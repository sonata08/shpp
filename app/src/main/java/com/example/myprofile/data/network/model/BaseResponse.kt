package com.example.myprofile.data.network.model


data class BaseResponse<out T>(
    val status: String = "",
    val code: String = "",
    val message: String? = null,
    val data: T
)

