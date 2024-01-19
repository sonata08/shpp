package com.example.myprofile.data.model

/**
 * Represents a user with additional properties for multiselect mode.
 *
 * @property contact The user information.
 * @property isSelected Indicates whether the user is selected.
 * @property isMultiselectMode Indicates whether the multiselect mode is active.
 *
 * @constructor Creates a UserMultiselect with the given user information.
 */
data class UserMultiselect(
    val contact: User,
    var isSelected: Boolean = false,
    var isMultiselectMode: Boolean = false
) {
    constructor(user: User) : this(user, false, false)
}