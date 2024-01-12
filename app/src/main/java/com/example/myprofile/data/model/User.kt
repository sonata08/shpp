package com.example.myprofile.data.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long = -1,
    val email: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val address: String? = "",
    val career: String? = "",
    val birthday: String? = "",
    val facebook: String? = "",
    val instagram: String? = "",
    val twitter: String? = "",
    val linkedin: String? = "",
    val image: String? = "",
) {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }
}
