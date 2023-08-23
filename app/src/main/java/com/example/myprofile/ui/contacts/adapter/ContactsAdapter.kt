package com.example.myprofile.ui.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.ItemContactBinding
import com.example.myprofile.utils.extentions.loadImage

class ContactsAdapter(
    private val onDeleteClickListener: (contact: Contact) -> Unit
) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(
        ContactsDiffCallBack()
    ) {
    class ContactViewHolder(
        private val binding: ItemContactBinding,
        val onDeleteClickListener: (contact: Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.apply {
                profilePic.loadImage(contact.photo)
                tvName.text = contact.name
                tvCareer.text = contact.career
                icDelete.setOnClickListener {
                    onDeleteClickListener(contact)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, onDeleteClickListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun getContactAtPosition(position: Int): Contact {
        return getItem(position)
    }

    private class ContactsDiffCallBack : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}