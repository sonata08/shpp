package com.example.myprofile.data.network.model


/**
 * Generic class representing a response from the server.
 */
data class BaseResponse<out T>(
    val status: String = "",
    val code: String = "",
    val message: String? = null,
    val data: T
)

