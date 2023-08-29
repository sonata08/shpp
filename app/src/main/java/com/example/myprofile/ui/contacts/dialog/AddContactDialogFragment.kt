package com.example.myprofile.ui.contacts.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.extentions.loadImage

class AddContactDialogFragment : DialogFragment() {

    private val binding: DialogAddContactBinding by lazy {
        DialogAddContactBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var addContactCallback: AddContactCallback

    // Instantiating the AddContactCallback to survive during screen rotations
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            addContactCallback = context as AddContactCallback
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement AddContactCallback")
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.photo.loadImage(null)
        binding.saveButton.setOnClickListener {
            if (isValidUsername()) {
                val contact = getContact()
                addContactCallback.onContactAdded(contact)
                dismiss()
            }
        }
        binding.backButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    private fun getContact(): Contact {
        with(binding) {
            val username = usernameEdit.text.toString()
            val career = careerEdit.text.toString()
            val email = emailEdit.text.toString()
            val phone = phoneEdit.text.toString()
            val address = addressEdit.text.toString()
            val birthDate = birthdateEdit.text.toString()
            return Contact(username, career, email, phone, address, birthDate)
        }
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