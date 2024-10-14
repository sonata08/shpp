package com.example.myprofile.data.network.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.model.UiState
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {
    val userContactsFlow: StateFlow<List<UserMultiselect>>
    suspend fun getUsers(): UiState<List<User>>
    suspend fun getUserContacts(): UiState<List<User>>
    suspend fun getContact(contactId: Long): UiState<User>
    suspend fun addContact(contactId: Long): UiState<List<User>>
    suspend fun deleteContact(contactId: Long): UiState<List<User>>
    suspend fun deleteContacts(): UiState<List<User>>
    suspend fun restoreLastDeletedContact()
    suspend fun logOut()
    fun updateContactsFlow(users: List<UserMultiselect>)
}