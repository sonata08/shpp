package com.example.myprofile.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.databinding.ActivityContactsBinding
import com.example.myprofile.ui.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.contacts.dialog.AddContactCallback
import com.example.myprofile.ui.contacts.dialog.AddContactDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity(), AddContactCallback {

    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(
            ContactsRepository()
        )
    }
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = ContactsAdapter {contact ->
            val position = adapter.currentList.indexOf(contact)
            deleteContactWithSnackbar(contact, position, binding.recyclerView)
        }
        setupRecyclerView()

            val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        binding.tvAddContact.setOnClickListener {

            Toast.makeText(this, "DIALOG", Toast.LENGTH_SHORT).show()
//            val dialogFragment = AddContactDialogFragment {
//                viewModel.addContact(it)
//            }
//            dialogFragment.show(supportFragmentManager, "AddContactDialog")
            showDialog(isLargeLayout)
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

    private fun deleteContactWithSnackbar(contact: Contact, position: Int, view: View) {
        viewModel.deleteContact(contact)
        Snackbar.make(view, R.string.contact_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModel.addContact(contact, position) }
            .show()
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val contact = adapter.getContactAtPosition(position)
            deleteContactWithSnackbar(contact, position, viewHolder.itemView)
        }
    }

//    override fun onContactAdded(contact: Contact) {
//        viewModel.addContact(contact)
//    }

    private fun showDialog(isLargeLayout: Boolean) {
        val fragmentManager = supportFragmentManager
        val newFragment = AddContactDialogFragment()
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog")
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


}

