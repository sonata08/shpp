package com.example.myprofile.utils

import android.util.Patterns

object Validation {

    const val MIN_PASSWORD_LENGTH = 3
    const val MAX_PASSWORD_LENGTH = 16

    /**
     * Validates if the given string is a valid email address.
     *
     * @param email The email address to be validated.
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Validates if the given password has a valid length.
     *
     * @param password The password to be validated.
     * @return `true` if the password has a valid length, `false` otherwise.
     */
    fun isValidPassword(password: String): Boolean {
        return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH
    }
}