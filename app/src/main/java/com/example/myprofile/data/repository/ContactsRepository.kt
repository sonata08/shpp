package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.model.ContactMultiselect
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {
    fun getContacts(): StateFlow<List<ContactMultiselect>>
    fun deleteContact(contactPosition: Int)
    fun deleteContacts()
    fun restoreLastDeletedContact()
    fun addContact(contact: Contact, index: Int)

    fun makeSelected(contactPosition: Int, isChecked: Boolean)

    fun countSelected(): Int

    fun deactivateMultiselectMode()
    fun activateMultiselectMode()
}