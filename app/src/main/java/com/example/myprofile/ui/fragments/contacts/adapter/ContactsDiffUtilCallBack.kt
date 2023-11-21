package com.example.myprofile.ui.fragments.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.ContactMultiselect

class ContactsDiffUtilCallBack : DiffUtil.ItemCallback<ContactMultiselect>() {
    override fun areItemsTheSame(oldItem: ContactMultiselect, newItem: ContactMultiselect): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ContactMultiselect, newItem: ContactMultiselect): Boolean {
        return oldItem == newItem
    }
}