package com.example.myprofile.ui.contacts

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
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.utils.extentions.loadImage

class DetailViewFragment : Fragment() {

    private var _binding: FragmentDetailViewBinding? = null
    private val binding get() = _binding!!
    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: ContactsViewModel by navGraphViewModels(
        R.id.contactsFragment,
        factoryProducer = {
            ContactsViewModelFactory(
                ContactsRepository()
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
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_left)

        return binding.root
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
        Log.d("M_DetailFrag", args.contactId.toString())
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