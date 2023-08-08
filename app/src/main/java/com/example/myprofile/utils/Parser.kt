package com.example.myprofile.utils

object Parser {

     fun parseEmail(email: String): String {
        val emailName = email.substringBefore("@")
        return if (emailName.contains(".")) {
            val name = emailName
                .split(".")
                .joinToString(separator = " ") { it -> it.replaceFirstChar { it.uppercase() } }
            name
        } else {
            emailName
        }
    }
}