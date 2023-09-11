package com.example.myprofile.ui.activities.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.ContactsRepositoryImpl

class ContactsViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

    val contactsFlow = contactsRepository.getContacts()

    fun deleteContact(contactPosition: Int) {
        contactsRepository.deleteContact(contactPosition)
    }

    fun addContact(contact: Contact,index: Int = contactsFlow.value.size) {
        contactsRepository.addContact(contact, index)
    }

    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }

}

class ContactsViewModelFactory(private val contactsRepository: ContactsRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(contactsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
