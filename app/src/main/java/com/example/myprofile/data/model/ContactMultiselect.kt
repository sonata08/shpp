package com.example.myprofile.data.model

data class ContactMultiselect(
    val contact: Contact,
    var isSelected: Boolean = false,
    var isMultiselectMode: Boolean = false
)

data class UserMultiselect(
    val contact: User,
    var isSelected: Boolean = false,
    var isMultiselectMode: Boolean = false
) {
    constructor(user: User) : this(user, false, false)
}