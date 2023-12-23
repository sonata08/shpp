package com.example.myprofile.data.network.dto


data class UserGet(
    val id: Long,
    val name: String?,
    val email: String,
    val phone: String?,
    val career: String?,
    val address: String?,
    val birthday: String?,
    val facebook: String?,
    val instagram: String?,
    val twitter: String?,
    val linkedin: String?,
    val image: String?,
)

data class UserGetBody(
    val status: String,
    val code: Int,
    val message: String,
    val data: UserGet,
)