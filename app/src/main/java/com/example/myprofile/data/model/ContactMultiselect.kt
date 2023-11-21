package com.example.myprofile.data.model

data class ContactMultiselect(
    val contact: Contact,
    var isSelected: Boolean = false,
    var isMultiselectMode: Boolean = false
)
