package com.example.myprofile.data.model


data class UserCredentials(
    val email: String,
    val password: String,
    val name: String = ""
)

data class UserCredentialsAuth(
    val email: String,
    val password: String,
)
