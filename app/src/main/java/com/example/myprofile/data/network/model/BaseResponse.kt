package com.example.myprofile.data.network.model

import com.example.myprofile.data.model.User

data class BaseResponse<out T>(
    val status: String,
    val code: String,
    val message: String?,
    val data: T
)

data class Users(
    val users: List<User>
)

data class Contacts(
    val contacts: List<User>
)

data class AddContactRequest (
    val contactId: Int
)