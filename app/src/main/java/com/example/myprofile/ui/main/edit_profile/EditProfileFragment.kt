package com.example.myprofile.ui.main.edit_profile


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
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentEditProfileBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.utils.extentions.loadImage
import com.example.myprofile.utils.showError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                viewModel.setPhotoUri(uri.toString())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeUiState()
        observePhoto()
        setListeners()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> bindUserData(viewModel.getUser())
                        is UiState.Success -> returnToSettingsFragment()
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(binding.root, binding.progressBar, it.message)
                    }
                }
            }
        }
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

    private fun bindUserData(user: User) {
        with(binding) {
            usernameEdit.setText(user.name)
            careerEdit.setText(user.career)
            phoneEdit.setText(user.phone)
            addressEdit.setText(user.address)
            birthdateEdit.setText(user.birthday)
        }
    }

    private fun setListeners() {
        with(binding) {
            btnSave.setOnClickListener {
                viewModel.updateUser(createUser())
            }
            btnAddPhoto.setOnClickListener {
                // Launches the photo picker and let the user choose image
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }
    }

    private fun createUser(): User {
        return with(binding) {
            User(
                name = usernameEdit.text.toString(),
                career = careerEdit.text.toString(),
                phone = phoneEdit.text.toString(),
                address = addressEdit.text.toString(),
                birthday = birthdateEdit.text.toString(),
                image = ""
            )
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun returnToSettingsFragment() {
        binding.progressBar.visibility = View.GONE
        findNavController().popBackStack()
    }

    private fun setupToolbar() {
        // when back button is pressed
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}