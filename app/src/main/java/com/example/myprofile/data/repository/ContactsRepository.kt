package com.example.myprofile.data.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.model.AuthUiStateTest
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {
    val userContactsFlow: StateFlow<List<UserMultiselect>>
    suspend fun getUsers(): AuthUiStateTest<List<User>>
    suspend fun getUserContacts(): AuthUiStateTest<List<User>>
    suspend fun getContact(contactId: Long): AuthUiStateTest<User>
    suspend fun addContact(contactId: Long): AuthUiStateTest<List<User>>
    suspend fun deleteContact(contactId: Long): AuthUiStateTest<List<User>>
    suspend fun deleteContacts(): AuthUiStateTest<List<User>>
    suspend fun restoreLastDeletedContact()
    fun makeSelected(contactPosition: Int, isChecked: Boolean)
    fun countSelectedItems(): Int
    fun deactivateMultiselectMode()
    fun activateMultiselectMode()
}