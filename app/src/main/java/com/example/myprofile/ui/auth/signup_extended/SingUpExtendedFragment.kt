package com.example.myprofile.ui.auth.signup_extended

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentSignUpExtendedBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.MainActivity
import com.example.myprofile.ui.utils.extentions.loadImage
import com.example.myprofile.utils.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SingUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {


    private val viewModel: SignUpExtendedViewModel by viewModels()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                viewModel.setPhotoUri(uri.toString())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        setListeners()
        observePhoto()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> singInUser()
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(binding.root, binding.progressBar, it.message)
                    }
                }
            }
        }
    }

    private fun singInUser() {
        binding.progressBar.visibility = View.GONE
        goToProfile()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    private fun observePhoto() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photoUri.collect {
                    binding.photo.loadImage(it)
                }
            }
        }
    }

    private fun setListeners() {
        setupForwardBtnListener()
        with(binding) {
            btnAddPhoto.setOnClickListener {
                // Launches the photo picker and let the user choose image
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnCancel.setOnClickListener {
                goToProfile()
            }
            phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }
    }

    private fun setupForwardBtnListener() {
        binding.btnForward.setOnClickListener {
            val user = createUpdatedUser()
            user?.let { viewModel.editUser(it) } ?: goToProfile()
        }
    }

    private fun createUpdatedUser(): User? {
        val name = binding.usernameEdit.text.toString()
        val phone = binding.phoneEdit.text.toString()
        return if (name.isEmpty() && phone.isEmpty()) {
            null
        } else {
            User(name = name, phone = phone)
        }
    }

    private fun goToProfile() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}