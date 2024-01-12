package com.example.myprofile.ui.main.detail_view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.utils.extentions.loadImage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailViewFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: DetailViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_left)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeUiState()
        getUser()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getUser() {
        viewModel.getContact(args.contactId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect {
                    when (it) {
                        is AuthUiStateTest.Initial -> {}
                        is AuthUiStateTest.Success -> bindContact(it.data)
                        is AuthUiStateTest.Loading -> showProgressBar()
                        is AuthUiStateTest.Error -> showError(it.message)
                    }
                }
            }
        }
    }

    private fun bindContact(contact: User) {
        with(binding) {
            progressBar.visibility = View.GONE
            photo.loadImage(contact.image)
            tvUsername.text = contact.name
            tvCareer.text = contact.career
            tvAddress.text = contact.address
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        // TODO: think what to do in case of error
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_AddContactFrag", "UiState.Error = $error")
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .show()
    }
}