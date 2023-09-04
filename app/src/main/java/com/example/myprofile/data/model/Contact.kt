package com.example.myprofile.data.model

data class Contact(
    val id: Long,
    val username: String,
    val career: String,
    val photo: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val birthDate: String = ""
)