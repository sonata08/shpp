package com.example.myprofile.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.databinding.ActivityContactsBinding
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {

    private val binding: ActivityContactsBinding by lazy { ActivityContactsBinding.inflate(layoutInflater) }
    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val contactsService = ContactsService()
//        val contacts = contactsService.generateContacts()

        val adapter = ContactsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}

