package com.example.myprofile.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.utils.extentions.loadImage

class DetailViewFragment: Fragment() {

    private var _binding: FragmentDetailViewBinding? = null
    private val binding get() = _binding!!
    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            ContactsRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindContact()

    }

    private fun bindContact() {
        val contact = viewModel.getContact(args.contactId)
        if (contact != null) {
            with(binding)
            {
                photo.loadImage(contact.photo)
                tvUsername.text = contact.username
                tvCareer.text = contact.career
                tvAddress.text = contact.address
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}