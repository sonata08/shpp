package com.example.myprofile.data.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The [UserInfoHolder] class serves as a centralized storage for user information
 * during the application's runtime.
 *
 * @property user StateFlow<User> - Immutable Flow providing access to user information.
 * @property userContacts StateFlow<List<User>> - Immutable Flow providing access to the list of user contacts.
 */
object UserInfoHolder {

    var user = User()
    var userContacts = listOf<User>()
}