package com.example.myprofile.ui.main.detail_view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.contacts.ContactsViewModel
import com.example.myprofile.ui.utils.extentions.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailViewFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_left)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        bindContact()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindContact() {
        val contact = viewModel.getContact(args.contactId)
        contact?.let {
            with(binding) {
                photo.loadImage(it.photo)
                tvUsername.text = it.username
                tvCareer.text = it.career
                tvAddress.text = it.address
            }
        }
    }
}