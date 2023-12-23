package com.example.myprofile.data.model

import kotlinx.serialization.Serializable

data class User(
    val id: Long,
    val email: String,
    val name: String?,
    val phone: String?,
    val address: String?,
    val career: String?,
    val birthday: String?,
    val facebook: String?,
    val instagram: String?,
    val twitter: String?,
    val linkedin: String?,
    val image: String?,


)
