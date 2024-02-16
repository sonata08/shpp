package com.example.myprofile.data.model

data class ContactMultiselect(
    val contact: Contact,
    val isSelected: Boolean = false,
    val isMultiselectMode: Boolean = false,
)
