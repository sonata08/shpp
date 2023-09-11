package com.example.myprofile.ui.activities.contacts.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.utils.Validation
import com.example.myprofile.ui.utils.extensions.loadImage

class AddContactDialogFragment : DialogFragment() {

    private val binding: DialogAddContactBinding by lazy {
        DialogAddContactBinding.inflate(
            layoutInflater
        )
    }
    private var photoUri = ""
    private lateinit var addContactCallback: AddContactCallback

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                photoUri = uri.toString()
                binding.photo.loadImage(photoUri)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addContactCallback = requireActivity() as AddContactCallback

        binding.photo.loadImage(photoUri)
        binding.btnAddPhoto.setOnClickListener {
            // Launch the photo picker and let the user choose image
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSave.setOnClickListener {
            if (isValidUsername()) {
                val contact = getContact(photoUri)
                addContactCallback.onContactAdded(contact)
                dismiss()
            }
        }
        binding.backButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    private fun getContact(photoUri: String): Contact = with(binding) {
        Contact(
            username = usernameEdit.text.toString(),
            career = careerEdit.text.toString(),
            photo = photoUri,
            email = emailEdit.text.toString(),
            phone = phoneEdit.text.toString(),
            address = addressEdit.text.toString(),
            birthDate = birthdateEdit.text.toString()

        )
    }

    private fun isValidUsername(): Boolean {
        with(binding) {
            val username = usernameEdit.text.toString()
            if (!Validation.isValidUsername(username)) {
                usernameEdit.error = getString(R.string.error_username)
                return false
            } else {
                usernameEdit.error = null
                return true
            }
        }
    }


}