package com.example.myprofile.ui.main.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.UserMultiselect

class ContactsDiffUtilCallBack : DiffUtil.ItemCallback<UserMultiselect>() {
    override fun areItemsTheSame(oldItem: UserMultiselect, newItem: UserMultiselect): Boolean {
        return oldItem.contact.id == newItem.contact.id
    }

    override fun areContentsTheSame(oldItem: UserMultiselect, newItem: UserMultiselect): Boolean {
        return oldItem == newItem
    }
}