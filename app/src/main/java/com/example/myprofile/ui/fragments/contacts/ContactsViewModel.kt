package com.example.myprofile.ui.fragments.contacts

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val contactsFlow = contactsRepository.getContacts()

    private var _photoUri = MutableStateFlow("")
    val photoUri = _photoUri.asStateFlow()

    fun deleteContacts() {
        contactsRepository.deleteContacts()
    }

    fun makeSelected(contactPosition: Int, isChecked: Boolean) {
        contactsRepository.makeSelected(contactPosition, isChecked)
    }

    fun isNothingSelected(): Boolean {
        if (contactsRepository.countSelected() == 0) {
            deactivateMultiselectMode()
            return true
        }
        return false
    }

    fun activateMultiselectMode(contactPosition: Int) {
        contactsRepository.activateMultiselectMode()
        makeSelected(contactPosition, true)
    }

    fun deactivateMultiselectMode() {
        contactsRepository.deactivateMultiselectMode()
    }

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
        return contactsFlow.value.find { it.contact.id == id }?.contact
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
