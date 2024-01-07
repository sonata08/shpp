package com.example.myprofile.ui.fragments.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ContactsViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

    val contactsFlow = contactsRepository.getContacts()

    private var _photoUri = MutableStateFlow("")
    val photoUri = _photoUri.asStateFlow()


    fun deleteContact(contactPosition: Int) {
        contactsRepository.deleteContact(contactPosition)
    }

    fun addContact(contact: Contact, index: Int = contactsFlow.value.size) {
        contactsRepository.addContact(contact, index)
    }

    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }

    fun getContact(id: Long): Contact? {
        return contactsFlow.value.find { it.id == id }
    }

    fun getNextId(): Long {
        return contactsFlow.value.size.toLong() + 1
    }


    fun setPhotoUri(uri: String) {
        _photoUri.value = uri
    }

    fun resetPhotoUri() {
        _photoUri.value = ""
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
