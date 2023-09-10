package com.example.myprofile.ui.activities.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val contactsFlow = contactsRepository.contactsFlow

    fun deleteContact(contact: Contact) {
        contactsRepository.deleteContact(contact)
    }


    fun deleteContact(contactPosition: Int) {
        contactsRepository.deleteContact(contactPosition)
    }

    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }
    fun addContact(contact: Contact, index: Int = contactsFlow.value.size) {
        contactsRepository.addContact(contact, index)
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
