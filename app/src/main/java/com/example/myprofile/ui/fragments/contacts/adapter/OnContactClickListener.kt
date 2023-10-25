package com.example.myprofile.ui.fragments.contacts.adapter

import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.Contact

interface OnContactClickListener {
     fun onContactDelete(contactPosition: Int)
     fun onContactClick(contact: Contact, extras: FragmentNavigator.Extras)

     fun onContactLongClick(contact: Contact)
}