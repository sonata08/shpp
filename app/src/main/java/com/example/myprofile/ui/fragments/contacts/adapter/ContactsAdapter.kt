package com.example.myprofile.ui.fragments.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.ItemContactBinding
import com.example.myprofile.ui.utils.extentions.loadImage

class ContactsAdapter(
    private val listener: OnContactClickListener
) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(
        ContactsDiffUtilCallBack()
    ) {
    inner class ContactViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentContact: Contact? = null
        private val profilePicImageView: ImageView = binding.root.findViewById(R.id.profile_pic)

        init { // TODO: why?

        }

        fun bind(contact: Contact) {
            currentContact = contact
            setListeners(contact)
            binding.apply {
                profilePic.loadImage(contact.photo)
                tvUsername.text = contact.username
                tvCareer.text = contact.career
            }
        }

        private fun setListeners(contact: Contact) {
            with(binding) {
                icDelete.setOnClickListener {
                    listener.onContactDelete(bindingAdapterPosition)

                }
                root.setOnClickListener {
                    val extras = FragmentNavigatorExtras(profilePicImageView to "detail_photo")
                    listener.onContactClick(contact, extras)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}