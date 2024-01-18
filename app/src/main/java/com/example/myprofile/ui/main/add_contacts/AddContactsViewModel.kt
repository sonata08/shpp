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

    private val _searchResult = MutableStateFlow(emptyList<User>())
    val searchResult = _searchResult.asStateFlow()

    fun getContacts() {
        viewModelScope.launch {
            val uiState = contactsRepository.getUsers()
            _uiStateFlow.value = uiState
            if (uiState is UiState.Success) {
                _searchResult.value = uiState.data
            }
        }
    }

    fun addContactToUser(contactId: Long) {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.addContact(
                contactId = contactId
            )
        }
    }

    fun filterUsers(query: String?) {
        if (_uiStateFlow.value is UiState.Success) {
            _searchResult.value = (_uiStateFlow.value as UiState.Success<List<User>>).data
        }
        if (!query.isNullOrBlank()) {
            val filteredUsers = _searchResult.value.filter { user ->
                (user.name?.contains(query, ignoreCase = true) ?: false) ||
                (user.career?.contains(query, ignoreCase = true) ?: false)
            }
            _searchResult.value = filteredUsers
        }
    }
}