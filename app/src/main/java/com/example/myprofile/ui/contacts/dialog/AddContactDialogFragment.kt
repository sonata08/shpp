package com.example.myprofile.ui.contacts.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.DialogAddContactBinding
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
            // Instantiate the AddContactCallback so we can send events to the host
            addContactCallback = context as AddContactCallback
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement AddContactCallback"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.photo.loadImage(null)
        binding.saveButton.setOnClickListener {
            val name = binding.usernameEdit.text.toString()
            val career = binding.careerEdit.text.toString()
            val contact = Contact(name, career)

            addContactCallback.onContactAdded(contact)

            dismiss()
        }


        return binding.root
    }


//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        // The only reason you might override this method when using onCreateView() is
//        // to modify any dialog characteristics. For example, the dialog includes a
//        // title by default, but your custom layout might not need it. So here you can
//        // remove the dialog title, but you must call the superclass to get the Dialog.
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        return dialog
//    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(requireContext())
//
//        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_contact, null)
//        builder.setView(view)
//
//        // Настройте кнопки и обработчики событий
//
//        return builder.create()
//        }

//        val builder = AlertDialog.Builder(requireContext())
//        // Get the layout inflater
//        val inflater = requireActivity().layoutInflater;
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.dialog_add_contact, null))
//        builder.create()
//
//        return builder

    override fun onDestroyView() {
        super.onDestroyView()
    }

}