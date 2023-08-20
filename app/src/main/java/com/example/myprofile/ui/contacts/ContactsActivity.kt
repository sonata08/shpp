package com.example.myprofile.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.databinding.ActivityContactsBinding
import com.example.myprofile.ui.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.contacts.adapter.ContactsItemDecoration
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {

    private val binding: ActivityContactsBinding by lazy { ActivityContactsBinding.inflate(layoutInflater) }
    private val viewModel: ContactsViewModel by viewModels {  ContactsViewModelFactory(
        ContactsRepository()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ContactsAdapter {
            viewModel.deleteContact(it)
            Toast.makeText(this,"DELETE", Toast.LENGTH_SHORT).show()
        }
        val itemDecoration = ContactsItemDecoration(R.dimen.margin_between_items, R.dimen.margin_last_item_bottom, R.drawable.recyclerview_item_shape)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}

