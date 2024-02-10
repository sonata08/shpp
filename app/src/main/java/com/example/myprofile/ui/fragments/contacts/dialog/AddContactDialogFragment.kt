package com.example.myprofile.ui.fragments.contacts.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.ui.fragments.contacts.ContactsViewModel
import com.example.myprofile.utils.Validation
import com.example.myprofile.ui.utils.extentions.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactDialogFragment : DialogFragment() {

    private val binding: DialogAddContactBinding by lazy {
        DialogAddContactBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: ContactsViewModel by viewModels()

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                viewModel.setPhotoUri(uri.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setListeners()
    }


    // makes dialog fullscreen if small layout and not fullscreen otherwise
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        return if (isLargeLayout) {
            Dialog(requireContext(), R.style.SmallScreenDialogStyle)
        } else {
            Dialog(requireContext(), R.style.FullScreenDialogStyle)
        }
    }


    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.resetPhotoUri()
            dismiss()
        }
    }

    private fun setListeners() {
        setupPhotoListener()
        setupAddPhotoListener()
        setupSaveBtnListener()
    }

    private fun setupPhotoListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photoUri.collect {
                    binding.photo.loadImage(it)
                }
            }
        }
    }

    private fun setupAddPhotoListener() {
        binding.btnAddPhoto.setOnClickListener {
            // Launches the photo picker and let the user choose image
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupSaveBtnListener() {
        binding.btnSave.setOnClickListener {
            if (isValidUsername()) {
                val contact = createContact()
                viewModel.addContact(contact)
                viewModel.resetPhotoUri()
                dismiss()
            }
        }
    }

    private fun createContact(): Contact {
        val uri = viewModel.photoUri.value
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
            if (!Validation.isValidUsername(username)) {
                usernameEdit.error =
                    getString(R.string.error_username, Validation.MIN_USERNAME_LENGTH)
                return false
            } else {
                usernameEdit.error = null
                return true
            }
        }
    }
}