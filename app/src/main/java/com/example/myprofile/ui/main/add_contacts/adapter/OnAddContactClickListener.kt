package com.example.myprofile.ui.main.add_contacts.adapter

import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.User

interface OnAddContactClickListener {
    fun onContactClick(contact: User, extras: FragmentNavigator.Extras)
    fun onAddContact(contactId: Long)
    fun onContactDelete(contactId: Long)
}