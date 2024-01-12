package com.example.myprofile.ui.main.detail_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<AuthUiStateTest<User>>(AuthUiStateTest.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun getContact(id: Long) {
        viewModelScope.launch {
            _uiStateFlow.value = contactsRepository.getContact(id)
        }
    }
}