package com.example.myprofile.ui.fragments.auth.signup_extended

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myprofile.databinding.FragmentSignUpExtendedBinding
import com.example.myprofile.ui.fragments.BaseFragment
import com.example.myprofile.utils.extentions.showShortToast


class SingUpExtendedFragment : BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {

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
    }
}