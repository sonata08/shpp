package com.example.myprofile.data.network.model


data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: List<Any>
)

