package com.example.myprofile.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {

    private val binding: ActivityContactsBinding by lazy { ActivityContactsBinding.inflate(layoutInflater) }
    private val viewModel: ContactsViewModel by viewModels {  ContactsViewModelFactory(
        ContactsRepository()
    ) }
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = ContactsAdapter {
            val position = adapter.currentList.indexOf(it)
            deleteContactWithSnackbar(it, position, binding.recyclerView)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)

        val itemDecoration = ContactsItemDecoration(R.dimen.margin_between_items, R.dimen.margin_last_item_bottom, R.drawable.recyclerview_item_shape)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun deleteContactWithSnackbar(contact: Contact, position: Int, view: View){
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


}

