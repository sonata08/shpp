package com.example.myprofile.ui.auth.signup_extended

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myprofile.data.model.User
import com.example.myprofile.databinding.FragmentSignUpExtendedBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.utils.extentions.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SingUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {

    val args: SingUpExtendedFragmentArgs by navArgs()
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
        setListeners()
        observePhoto()
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
        val user = createUpdatedUser()
        binding.btnForward.setOnClickListener {
            user?.let { viewModel.editUser(it) }
            goToProfile()
        }
    }

    private fun createUpdatedUser(): User? {
        val name = binding.usernameEdit.text.toString()
        val phone = binding.phoneEdit.text.toString()
        return if (name.isNotEmpty() && phone.isNotEmpty()) {
            User(name = name, phone = phone)
        } else {
            null
        }
    }

    private fun goToProfile() {
        val action = SingUpExtendedFragmentDirections.actionSingUpExtendedFragmentToMainActivity()
        findNavController().navigate((action))
    }
}