package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.model.LocalContactsDataSource

class ContactsRepository {

    private val localContactsDataSource = LocalContactsDataSource()
//    val contactsList = localContactsDataSource.contactsList

    fun getContacts() = localContactsDataSource.contactsList
    fun deleteContact(contact: Contact) {
        localContactsDataSource.contactsList.remove(contact)
    }

    fun addContact(contact: Contact, index: Int = getContacts().size) {
        localContactsDataSource.contactsList.add(index, contact)
    }
}