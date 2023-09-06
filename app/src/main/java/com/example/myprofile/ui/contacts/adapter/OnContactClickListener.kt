package com.example.myprofile.ui.contacts.adapter

import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.Contact

interface OnContactClickListener {
     fun onContactDelete(contact: Contact)
     fun onContactClick(contact: Contact, extras: FragmentNavigator.Extras)
}