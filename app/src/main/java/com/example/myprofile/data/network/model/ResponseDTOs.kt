package com.example.myprofile.data.network.model

import com.example.myprofile.data.model.User

/**
 * File containing data classes that can be included to [BaseResponse].
 */

data class LoginResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)

data class Users(
    val users: List<User>
)

data class UserGet(
    val user: User
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class Contacts(
    val contacts: List<User>
)

data class AddContactRequest (
    val contactId: Int
)