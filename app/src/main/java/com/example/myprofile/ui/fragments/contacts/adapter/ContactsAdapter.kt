package com.example.myprofile.ui.fragments.contacts.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.ContactMultiselect
import com.example.myprofile.databinding.ItemContactBinding
import com.example.myprofile.ui.utils.extentions.loadImage

const val TRANSITION_NAME = "detail_photo"
const val TAG = "FAT_ADAPTER"

class ContactsAdapter(
    private val listener: OnContactClickListener
) :
    ListAdapter<ContactMultiselect, ContactsAdapter.ContactViewHolder>(
        ContactsDiffUtilCallBack()
    ) {

    inner class ContactViewHolder(
        private val binding: ItemContactBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val profilePicImageView: ImageView = binding.root.findViewById(R.id.profile_pic)

        init {

        }

        fun bind(contact: ContactMultiselect) {
            binding.apply {
                profilePic.loadImage(contact.contact.photo)
                tvUsername.text = contact.contact.username
                tvCareer.text = contact.contact.career
                isSelectedCheckbox.isChecked = contact.isSelected
                setupMultiselectMode(contact)
            }
            setListeners(contact)
        }

        private fun setupMultiselectMode(contact: ContactMultiselect) {
            with(binding) {
                if (contact.isMultiselectMode) {
                    isSelectedCheckbox.visibility = View.VISIBLE
                    icDelete.visibility = View.GONE
                    root.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.recyclerview_item_shape_multiselect)
                } else {
                    isSelectedCheckbox.visibility = View.GONE
                    icDelete.visibility = View.VISIBLE
                    root.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.recyclerview_item_shape)
                }
            }
        }


        private fun setListeners(contact: ContactMultiselect) {
            with(binding) {
                icDelete.setOnClickListener {
                    // checks if item has not been removed from the adapter
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onContactDelete(bindingAdapterPosition)
                    }
                }
                root.setOnClickListener {
                    val extras = FragmentNavigatorExtras(profilePicImageView to TRANSITION_NAME)
                    listener.onContactClick(contact, extras)
                }
                root.setOnLongClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onContactLongClick(bindingAdapterPosition)
                    }
                    true
                }
                isSelectedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    listener.onItemSelect(bindingAdapterPosition, isChecked)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


}