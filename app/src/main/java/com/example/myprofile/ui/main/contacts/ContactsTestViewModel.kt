package com.example.myprofile.ui.main.contacts

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.impl.ContactsTestRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsTestViewModel @Inject constructor(
) : ViewModel() {

    private val contactsRepository = ContactsTestRepositoryImpl()

    val contactsFlow = contactsRepository.getContacts()

    private var _photoUri = ""
    val photoUri: String
        get() = _photoUri

    // position of the first selected item in multiselect mode
    // or 0 if not in multiselect mode
    var scrollPosition = 0

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
        scrollPosition = contactPosition
    }

    fun deactivateMultiselectMode() {
        contactsRepository.deactivateMultiselectMode()
        scrollPosition = 0
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
        _photoUri = uri
    }

    fun resetPhotoUri() {
        _photoUri = ""
    }
}
