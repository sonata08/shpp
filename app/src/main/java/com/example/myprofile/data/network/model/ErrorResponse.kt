package com.example.myprofile.data.network.model

import androidx.annotation.Keep

@Keep
data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: List<Any>
)

