package com.example.myprofile.ui.main.add_contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.ContactMultiselect
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect

class ContactsDiffUtilCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}