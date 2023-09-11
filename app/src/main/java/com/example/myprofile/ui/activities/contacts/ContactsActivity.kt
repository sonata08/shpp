package com.example.myprofile.ui.activities.contacts

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepositoryImpl
import com.example.myprofile.databinding.ActivityContactsBinding
import com.example.myprofile.ui.activities.signup.SignUpActivity
import com.example.myprofile.ui.activities.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.activities.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.activities.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.activities.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.activities.contacts.dialog.AddContactCallback
import com.example.myprofile.ui.activities.contacts.dialog.AddContactDialogFragment
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ContactsActivity : AppCompatActivity(), AddContactCallback, OnContactClickListener {

    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(
            ContactsRepositoryImpl()
        )
    }
    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        binding.tvAddContact.setOnClickListener {
            showDialog(isLargeLayout)
        }

        // opens SingUpActivity just for convenience to check CustomView
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        val itemDecoration = ContactsItemDecoration(
            R.dimen.basic_layout_horizontal_margins,
            R.dimen.margin_between_items,
            R.dimen.margin_last_item_bottom,
            R.drawable.recyclerview_item_shape
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteContactWithSnackbar(position: Int) {
        viewModel.deleteContact(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        Snackbar.make(binding.root, R.string.contact_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModel.restoreLastDeletedContact() }
            .show()
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            deleteContactWithSnackbar(position)
        }
    }

    // shows fullscreen dialog if small layout and not fullscreen otherwise
    private fun showDialog(isLargeLayout: Boolean) {
        val fragmentManager = supportFragmentManager
        val newFragment = AddContactDialogFragment()
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme)
            newFragment.show(fragmentManager, ADD_CONTACT_DIALOG)
        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onContactAdded(contact: Contact) {
        viewModel.addContact(contact)
    }

    override fun onContactDelete(contactPosition: Int) {
        deleteContactWithSnackbar(contactPosition)
    }

    override fun onContactClick(contact: Contact) {
        showShortToast("Open ${contact.username}'s details")
    }

    companion object {
        const val ADD_CONTACT_DIALOG = "AddContactDialogFragment"
    }
}

