package com.example.myprofile.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

    private val _contactsFlow = MutableStateFlow(contactsRepository.getContacts().toList())
    val contactsFlow = _contactsFlow.asStateFlow()

    fun deleteContact(contact: Contact) {
        contactsRepository.deleteContact(contact)
        _contactsFlow.value = contactsRepository.getContacts().toList()
    }

    fun addContact(contact: Contact, index: Int = _contactsFlow.value.size) {
        contactsRepository.addContact(contact, index)
        _contactsFlow.value = contactsRepository.getContacts().toList()
    }
}

class ContactsViewModelFactory(private val contactsRepository: ContactsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(contactsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
