package com.example.myprofile.ui.activities.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.Contact

class ContactsDiffUtilCallBack : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}