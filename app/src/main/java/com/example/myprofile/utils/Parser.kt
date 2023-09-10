package com.example.myprofile.utils

object Parser {

     fun parseEmail(email: String): String {
        val emailName = email.substringBefore("@")      //todo to const
        return if (emailName.contains(".")) { //todo to const
            val name = emailName
                .split(".") //todo to const
                .joinToString(separator = " ") { it -> it.replaceFirstChar { it.uppercase() } }
            name
        } else {
            emailName
        }
    }
}