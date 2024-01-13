package com.example.myprofile.ui.main.edit_profile


import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentEditProfileBinding
import com.example.myprofile.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeUiState()
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
                        is UiState.Error -> showError(it.message)
                    }
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

    private fun showError(error: String) {
        // TODO: think what to do in case of error
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_SettingsFrag", "UiState.Error = $error")
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun returnToSettingsFragment() {
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_SettingsFrag", "UiState = Success")
        findNavController().popBackStack()
    }

    private fun setupToolbar() {
        // when back button is pressed
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}