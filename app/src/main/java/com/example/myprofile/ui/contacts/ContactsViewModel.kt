package com.example.myprofile.ui.contacts

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.ContactsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel() {

    private val _contactsFlow = MutableStateFlow(ContactsService().generateContacts())
    val contactsFlow = _contactsFlow.asStateFlow()
}

