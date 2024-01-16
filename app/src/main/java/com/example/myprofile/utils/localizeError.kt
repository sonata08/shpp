package com.example.myprofile.utils

import android.content.Context
import com.example.myprofile.R

/**
 * Shows error in user's language
 */
fun localizeError(error: String, context: Context): String {
    val errorMessage = when(error) {
        "User already exists" -> R.string.user_exists
        "Unauthorized" -> R.string.unauthorized
        "Access denied" -> R.string.access_denied
        "Invalid request" -> R.string.access_denied
        else-> R.string.default_error
    }
    return context.getString(errorMessage)

}