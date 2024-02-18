package com.example.myprofile.ui.main.contacts.adapter

import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.UserMultiselect

interface OnContactClickListener {
     fun onContactDelete(contactId: Long)
     fun onContactClick(contact: UserMultiselect, extras: FragmentNavigator.Extras)
     fun onContactLongClick(contactPosition: Int)
     fun onItemSelect(contactPosition: Int, isChecked: Boolean)
}