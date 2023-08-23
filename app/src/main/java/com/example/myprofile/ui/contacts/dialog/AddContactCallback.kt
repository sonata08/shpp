package com.example.myprofile.ui.contacts.dialog

import com.example.myprofile.data.model.Contact

interface AddContactCallback {
    fun onContactAdded(contact: Contact)
}