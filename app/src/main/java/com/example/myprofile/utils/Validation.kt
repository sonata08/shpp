package com.example.myprofile.utils

import android.util.Patterns

object Validation {

    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_PASSWORD_LENGTH = 16

    // Checks email validity
    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // Checks password validity
    fun isValidPassword(password: String): Boolean {
        return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH
    }
}