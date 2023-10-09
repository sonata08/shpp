package com.example.myprofile.ui.fragments.contacts.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.ui.fragments.contacts.ContactsViewModel
import com.example.myprofile.ui.fragments.contacts.ContactsViewModelFactory
import com.example.myprofile.utils.Validation
import com.example.myprofile.ui.utils.extentions.loadImage


class AddContactDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddContactBinding

    private val viewModel: ContactsViewModel by navGraphViewModels(
        R.id.contactsFragment,
        factoryProducer = {
            ContactsViewModelFactory(
                ContactsRepositoryImpl()
            )
        }
    )

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            viewModel.setPhotoUri(uri.toString())
            binding.photo.loadImage(viewModel.photoUri)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.resetPhotoUri()
            dismiss()
        }
    }

    // makes dialog fullscreen if small layout and not fullscreen otherwise
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_contact, null)
        builder.setView(dialogView)
        binding = DialogAddContactBinding.bind(dialogView)
        setupToolbar()
        binding.photo.loadImage(viewModel.photoUri)
        setupAddPhotoListener()
        setupSaveListener()
        return builder.create()
//        return if (isLargeLayout) {
//            Dialog(requireContext(), R.style.SmallScreenDialogStyle)
//        } else {
//            Dialog(requireContext(), R.style.FullScreenDialogStyle)
//        }
    }

    // Registers a photo picker activity launcher in single-select mode.
//    private fun registerPhotoPicker() {
//        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//            // Callback is invoked after the user selects a media item or closes the photo picker.
//            if (uri != null) {
//                viewModel.setPhotoUri(uri.toString())
//            }
//        }
//    }



    private fun setupAddPhotoListener() {
        binding.btnAddPhoto.setOnClickListener {
            // Launches the photo picker and let the user choose image
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupSaveListener() {
        binding.btnSave.setOnClickListener {
            if (isValidUsername()) {
                val contact = createContact(viewModel.photoUri)
                viewModel.addContact(contact)
                viewModel.resetPhotoUri()
                dismiss()
            }
        }
    }

    private fun createContact(uri: String): Contact {
        with(binding) {
            val id = viewModel.getNextId()
            val username = usernameEdit.text.toString()
            val career = careerEdit.text.toString()
            val email = emailEdit.text.toString()
            val phone = phoneEdit.text.toString()
            val address = addressEdit.text.toString()
            val birthDate = birthdateEdit.text.toString()
            return Contact(id, username, career, uri, email, phone, address, birthDate)
        }
    }

    private fun isValidUsername(): Boolean {
        with(binding) {
            val username = usernameEdit.text.toString()
            return if (!Validation.isValidUsername(username)) {
                usernameEdit.error =
                    getString(R.string.error_username, Validation.MIN_USERNAME_LENGTH)
                 false
            } else {
                usernameEdit.error = null
                true
            }
        }
    }
}