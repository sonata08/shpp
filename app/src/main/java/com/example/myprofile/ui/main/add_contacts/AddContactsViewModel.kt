package com.example.myprofile.ui.main.add_contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()


    fun getContacts() {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.getUsers()
        }
    }

    fun addContactToUser(contactId: Long) {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.addContact(
                contactId = contactId
            )
        }
    }
//
//    fun deleteContactFromUser(contactId: Long) {
//        viewModelScope.launch {
//            _uiStateFlow.value = contactsRepository.deleteContact(
//                contactId = contactId
//            )
//        }
//    }
}