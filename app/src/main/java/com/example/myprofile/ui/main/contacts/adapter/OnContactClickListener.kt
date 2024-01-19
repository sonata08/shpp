package com.example.myprofile.ui.main.contacts.adapter

import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.UserMultiselect

interface OnContactClickListener {
     fun onContactDelete(contactId: Long)
     fun onContactClick(contact: UserMultiselect, extras: FragmentNavigator.Extras)
     fun onContactLongClick(contactId: Long)
     fun onItemSelect(contactId: Long, isChecked: Boolean)
}