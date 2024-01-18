package com.example.myprofile.ui.main.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    val contactsListFlow = contactsRepository.userContactsFlow

    private val _searchResult = MutableStateFlow(emptyList<UserMultiselect>())
    val searchResult = _searchResult.asStateFlow()

    fun getUserContacts() {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.getUserContacts()
        }
    }

    fun deleteContactFromUser(contactId: Long) {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.deleteContact(
                contactId = contactId
            )
        }
    }

    fun deleteContacts() {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.deleteContacts()
        }
    }

    fun restoreLastDeletedContact() {
        viewModelScope.launch {
            contactsRepository.restoreLastDeletedContact()
        }
    }

    fun isNothingSelected(): Boolean {
        if (contactsRepository.countSelectedItems() == 0) {
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

    fun makeSelected(contactPosition: Int, isChecked: Boolean) {
        contactsRepository.makeSelected(contactPosition, isChecked)
    }

    fun filterUsers(query: String?) {
        _searchResult.value = contactsListFlow.value
        if (!query.isNullOrBlank()) {
            val filteredUsers = _searchResult.value.filter { user ->
                (user.contact.name?.contains(query, ignoreCase = true) ?: false) ||
                        (user.contact.career?.contains(query, ignoreCase = true) ?: false)
            }
            _searchResult.value = filteredUsers
        }
    }
}