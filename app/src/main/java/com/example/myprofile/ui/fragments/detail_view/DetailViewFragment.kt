package com.example.myprofile.ui.fragments.detail_view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.transition.TransitionInflater
import com.example.myprofile.R
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.ui.fragments.contacts.ContactsViewModel
import com.example.myprofile.ui.fragments.contacts.ContactsViewModelFactory
import com.example.myprofile.ui.utils.extentions.loadImage

class DetailViewFragment : Fragment() {

    private var _binding: FragmentDetailViewBinding? = null
    private val binding get() = _binding!!
    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: ContactsViewModel by navGraphViewModels(
        R.id.contactsFragment,
        factoryProducer = {
            ContactsViewModelFactory(
                ContactsRepositoryImpl()
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        // adds photo animation
        setSharedElement()

        return binding.root
    }

    private fun setSharedElement() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_left)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        bindContact()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { // TODO: read about
            findNavController().navigateUp()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}