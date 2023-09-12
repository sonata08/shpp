package com.example.myprofile.ui.activities.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.ItemContactBinding
import com.example.myprofile.ui.utils.extensions.loadImage

class ContactsAdapter(
    private val listener: OnContactClickListener
) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(
        ContactsDiffUtilCallBack()
    ) {
    class ContactViewHolder(
        private val binding: ItemContactBinding,
        private val listener: OnContactClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentContact: Contact? = null

        init {
            binding.root.setOnClickListener {
                currentContact?.let {
                    listener.onContactClick(it)
                }
            }
        }

        fun bind(contact: Contact, position: Int) {
            currentContact = contact
            binding.apply {
                profilePic.loadImage(contact.photo)
                tvName.text = contact.username
                tvCareer.text = contact.career
                icDelete.setOnClickListener {
                    listener.onContactDelete(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, position)
    }


}