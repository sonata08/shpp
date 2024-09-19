package com.example.myprofile.data.model

import androidx.annotation.Keep

/**
 * Represents a user with additional properties for multiselect mode.
 *
 * @property contact The user information.
 * @property isSelected Indicates whether the user is selected.
 * @property isMultiselectMode Indicates whether the multiselect mode is active.
 *
 * @constructor Creates a UserMultiselect with the given user information.
 */
@Keep
data class UserMultiselect(
    val contact: User,
    val isSelected: Boolean = false,
    val isMultiselectMode: Boolean = false,
) {
    constructor(user: User) : this(user, false, false)
}