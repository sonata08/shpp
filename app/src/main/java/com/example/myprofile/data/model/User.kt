package com.example.myprofile.data.model

import com.example.myprofile.data.network.INVALID_ID
import com.example.myprofile.data.room.entity.ContactEntity
import com.example.myprofile.data.room.entity.UserEntity

data class User(
    val id: Long = INVALID_ID,
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

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        name = this.name,
        phone = this.phone,
        address = this.address,
        career = this.career,
        birthday = this.birthday
    )
}

fun User.toContact(): ContactEntity {
    return ContactEntity (
        id = this.id,
        name = this.name,
        address = this.address,
        career = this.career,
    )
}