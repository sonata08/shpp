package com.example.myprofile.ui.fragments.contacts.adapter

import android.view.View
import androidx.navigation.fragment.FragmentNavigator
import com.example.myprofile.data.model.ContactMultiselect

interface OnContactClickListener {
     fun onContactDelete(contactPosition: Int)
     fun onContactClick(contact: ContactMultiselect, extras: FragmentNavigator.Extras)
     fun onContactLongClick(contactPosition: Int)
     fun onItemSelect(contactPosition: Int, isChecked: Boolean)

}