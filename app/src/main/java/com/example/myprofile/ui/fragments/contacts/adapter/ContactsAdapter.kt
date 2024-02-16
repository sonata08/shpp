package com.example.myprofile.ui.fragments.contacts.adapter


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

private const val TRANSITION_NAME = "detail_photo"

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

        fun bind(contact: ContactMultiselect) {
            binding.apply {
                profilePic.loadImage(contact.contact.photo)
                tvUsername.text = contact.contact.username
                tvCareer.text = contact.contact.career
                isSelectedCheckbox.isChecked = contact.isSelected
                setupMultiselectMode(contact)
            }
            setListeners()
        }

        private fun setupMultiselectMode(contact: ContactMultiselect) {
            with(binding) {
                if (contact.isMultiselectMode) {
                    isSelectedCheckbox.visibility = View.VISIBLE
                    icDelete.visibility = View.GONE
                    setItemBackground(R.drawable.recyclerview_item_shape_multiselect)
                } else {
                    isSelectedCheckbox.visibility = View.GONE
                    icDelete.visibility = View.VISIBLE
                    setItemBackground(R.drawable.recyclerview_item_shape)
                    setOnItemClickListener(contact)
                }
            }
        }

        private fun setItemBackground(drawable: Int) {
            binding.root.background = ContextCompat.getDrawable(
                binding.root.context,
                drawable)
        }

        private fun setOnItemClickListener(contact: ContactMultiselect) {
            binding.root.setOnClickListener {
                val extras = FragmentNavigatorExtras(profilePicImageView to TRANSITION_NAME)
                listener.onContactClick(contact, extras)
            }
        }

        private fun setListeners() {
            with(binding) {
                icDelete.setOnClickListener {
                    // checks if item has not been removed from the adapter
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onContactDelete(bindingAdapterPosition)
                    }
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