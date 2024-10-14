package com.example.myprofile.utils

import android.content.Context
import android.view.View
import com.example.myprofile.R
import com.example.myprofile.data.network.ACCESS_DENIED
import com.example.myprofile.data.network.INTERNET_CONNECTION_FAILED
import com.example.myprofile.data.network.INVALID_REQUEST
import com.example.myprofile.data.network.NO_USER_ERROR
import com.example.myprofile.data.network.UNAUTHORIZED
import com.example.myprofile.data.network.UNKNOWN_ERROR
import com.example.myprofile.data.network.USER_EXISTS
import com.example.myprofile.data.network.model.ErrorResponse
import com.example.myprofile.ui.utils.extentions.hide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.lang.Exception

/**
 * Displays an error message using Snackbar.
 */
fun showError(root: View, progressBar: View, error: String) {
    val localizedError = localizeError(error, root.context)
    progressBar.hide()
    Snackbar.make(root, localizedError, Snackbar.LENGTH_LONG)
        .show()
}

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

/**
 * Shows error in user's language
 */
fun localizeError(error: String, context: Context): String {
    val errorMessage = when(error) {
        USER_EXISTS -> R.string.user_exists
        UNAUTHORIZED -> R.string.unauthorized
        ACCESS_DENIED -> R.string.access_denied
        INVALID_REQUEST -> R.string.access_denied
        NO_USER_ERROR -> R.string.no_user
        INTERNET_CONNECTION_FAILED -> R.string.internet_connection_failed
        UNKNOWN_ERROR -> R.string.unknown_error
        else-> return error
    }
    return context.getString(errorMessage)

}