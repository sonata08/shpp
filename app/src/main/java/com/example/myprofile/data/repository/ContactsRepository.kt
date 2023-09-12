package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {
    fun getContacts(): StateFlow<List<Contact>>
    fun deleteContact(contactPosition: Int)
    fun restoreLastDeletedContact()
    fun addContact(contact: Contact, index: Int)
}