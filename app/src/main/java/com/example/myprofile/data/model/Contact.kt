package com.example.myprofile.data.model

data class Contact(
    val username: String,
    val career: String,
    val photo: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val birthDate: String? = null
)