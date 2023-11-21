package com.example.myprofile.ui.fragments.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository

//@HiltViewModel
//class ContactsViewModel @Inject constructor(
class ContactsViewModel (
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    //TODO: save sate isMultiSelectMode
//    private var isMultiSelectMode = false
//    fun toMultiSelectMode() {
//        isMultiSelectMode = !isMultiSelectMode
//    }

    //TODO: save list of selected items



    val contactsFlow = contactsRepository.getContacts()



    private var _photoUri = ""
    val photoUri: String
        get() = _photoUri




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
        _photoUri = uri
    }

    fun resetPhotoUri() {
        _photoUri = ""
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
