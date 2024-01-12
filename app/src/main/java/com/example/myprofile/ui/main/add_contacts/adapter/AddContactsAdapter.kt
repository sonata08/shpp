package com.example.myprofile.ui.main.add_contacts.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.User
import com.example.myprofile.databinding.ItemAddContactBinding
import com.example.myprofile.ui.utils.extentions.loadImage

private const val TRANSITION_NAME = "detail_photo"

class AddContactsAdapter(
    private val listener: OnAddContactClickListener
) :
    ListAdapter<User, AddContactsAdapter.ContactViewHolder>(
        ContactsDiffUtilCallBack()
    ) {

    inner class ContactViewHolder(
        private val binding: ItemAddContactBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val profilePicImageView: ImageView = binding.root.findViewById(R.id.profile_pic)

        fun bind(contact: User) {
            binding.apply {
                profilePic.loadImage(contact.image)
                tvUsername.text = contact.name
                tvCareer.text = contact.career
//                isSelectedCheckbox.isChecked = contact.isSelected
            }
            setListeners(contact)
        }

        private fun setListeners(contact: User) {
            with(binding) {
                addContactLayout.setOnClickListener {
                    addContactLayout.visibility = View.GONE
                    icAddContactDone.visibility = View.VISIBLE
                    listener.onAddContact(contact.id)
                }
                root.setOnClickListener {
                    val extras = FragmentNavigatorExtras(profilePicImageView to TRANSITION_NAME)
                    listener.onContactClick(contact, extras)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ItemAddContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}