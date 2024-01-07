package com.example.myprofile.ui.main.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserInfoHolder
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.network.model.Users
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {


    private val _authStateFlow = MutableStateFlow<AuthUiStateTest<List<User>>>(AuthUiStateTest.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

    fun getUserContacts() {
        viewModelScope.launch {
            _authStateFlow.value = contactsRepository.getUserContacts()
        }
    }

}