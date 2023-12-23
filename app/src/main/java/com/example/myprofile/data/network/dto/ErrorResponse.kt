package com.example.myprofile.data.network.dto


data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: List<Any>
)

