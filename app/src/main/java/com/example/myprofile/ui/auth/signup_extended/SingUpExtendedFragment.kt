package com.example.myprofile.ui.auth.signup_extended

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myprofile.data.model.User
import com.example.myprofile.databinding.FragmentSignUpExtendedBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.utils.extentions.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SingUpExtendedFragment : BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {

    val args: SingUpExtendedFragmentArgs by navArgs()
    val viewModel: SignUpExtendedViewModel by viewModels()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            requireContext().showShortToast("PHOTO")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddPhoto.setOnClickListener {
            // Launches the photo picker and let the user choose image
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        setupForwardBtnListener()
        setupCancelBtnListener()
    }

    private fun setupForwardBtnListener() {
        val user = createUpdatedUser()
        binding.btnForward.setOnClickListener {
            if (user != null) {
                Log.d("FAT_Extended", user.toString())
                viewModel.editUser(user)
            }
            goToProfile()
        }
    }

    private fun setupCancelBtnListener() {
        binding.btnCancel.setOnClickListener {
            goToProfile()
        }
    }

    private fun createUpdatedUser(): User? {
        val name = binding.usernameEdit.text.toString()
        val phone = binding.phoneEdit.text.toString()
        return if (name.isNotEmpty() && phone.isNotEmpty() ) {
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