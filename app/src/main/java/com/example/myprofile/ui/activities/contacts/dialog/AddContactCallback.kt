package com.example.myprofile.ui.activities.contacts.dialog

import com.example.myprofile.data.model.Contact

interface AddContactCallback {
    fun onContactAdded(contact: Contact)
}