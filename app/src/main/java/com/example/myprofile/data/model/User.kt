package com.example.myprofile.data.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long = -1,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val career: String? = null,
    val birthday: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val image: String? = null,
)