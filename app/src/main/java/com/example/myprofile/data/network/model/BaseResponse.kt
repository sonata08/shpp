package com.example.myprofile.data.network.model

import androidx.annotation.Keep


/**
 * Generic class representing a response from the server.
 */
@Keep
data class BaseResponse<out T>(
    val status: String = "",
    val code: String = "",
    val message: String? = null,
    val data: T
)

