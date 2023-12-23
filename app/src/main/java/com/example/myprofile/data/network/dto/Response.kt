package com.example.myprofile.data.network.dto

import kotlinx.serialization.Serializable

data class Response<T>(
    val status: String,
    val code: String,
    val message: String?,
    val data: T

)

