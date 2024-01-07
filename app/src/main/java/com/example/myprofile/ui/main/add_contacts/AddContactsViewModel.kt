package com.example.myprofile.ui.main.add_contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.data.network.model.Users
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiStateTest<List<User>>>(AuthUiStateTest.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()


    fun getContacts() {
        viewModelScope.launch {
            _authStateFlow.value = contactsRepository.getUsers()
        }
    }

    fun addContactToUser(contactId: Long) {
        viewModelScope.launch {
            _authStateFlow.value = contactsRepository.addContact(
                contactId = contactId
            )
        }
    }

    fun deleteContactFromUser(contactId: Long) {
        viewModelScope.launch {
            _authStateFlow.value = contactsRepository.deleteContact(
                contactId = contactId
            )
        }
    }
}