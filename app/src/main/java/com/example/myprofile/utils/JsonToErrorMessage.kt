package com.example.myprofile.utils

import com.example.myprofile.data.network.model.ErrorResponse
import com.google.gson.Gson
import java.lang.Exception


private const val UNKNOWN_ERROR = "Unknown error"
/**
 * Transforms JSON to ErrorResponse class and returns error message
 */
fun getMessageFromHttpException(jsonString: String?): String {
    return try {
        val loginResponse: ErrorResponse = Gson().fromJson(jsonString, ErrorResponse::class.java)
        loginResponse.message
    } catch (e: Exception) {
        UNKNOWN_ERROR
    }
}