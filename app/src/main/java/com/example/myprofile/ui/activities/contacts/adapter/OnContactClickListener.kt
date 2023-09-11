package com.example.myprofile.ui.activities.contacts.adapter

import com.example.myprofile.data.model.Contact

interface OnContactClickListener {
     fun onContactDelete(contactPosition: Int)
     fun onContactClick(contact: Contact)
}