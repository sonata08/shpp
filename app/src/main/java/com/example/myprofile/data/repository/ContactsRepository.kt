package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.network.model.Contacts
import com.example.myprofile.data.network.model.Users

interface ContactsRepository {
    suspend fun getUsers(): AuthUiStateTest<List<User>>
    suspend fun getUserContacts(): AuthUiStateTest<List<User>>
    suspend fun deleteContact(contactId: Long): AuthUiStateTest<List<User>>
    fun deleteContacts()
    fun restoreLastDeletedContact()
    suspend fun addContact(contactId: Long): AuthUiStateTest<List<User>>
    fun makeSelected(contactPosition: Int, isChecked: Boolean)
    fun countSelected(): Int
    fun deactivateMultiselectMode()
    fun activateMultiselectMode()
}