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
    class ContactViewHolder(
        private val binding: ItemContactBinding,
        private val listener: OnContactClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentContact: Contact? = null
        private val profilePicImageView: ImageView = binding.root.findViewById(R.id.profile_pic)

        init {
            binding.root.setOnClickListener {
                val extras = FragmentNavigatorExtras(profilePicImageView to "detail_photo")
                currentContact?.let {
                    listener.onContactClick(it, extras)
                }
            }
        }

        fun bind(contact: Contact) {
            currentContact = contact
            binding.apply {
                profilePic.loadImage(contact.photo)
                tvUsername.text = contact.username
                tvCareer.text = contact.career
                icDelete.setOnClickListener {
                    listener.onContactDelete(bindingAdapterPosition)
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
        holder.bind(current)
    }


}