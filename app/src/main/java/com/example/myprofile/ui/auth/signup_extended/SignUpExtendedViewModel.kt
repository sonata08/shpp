package com.example.myprofile.ui.auth.signup_extended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.network.dto.EditUser
import com.example.myprofile.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

    fun editUser(token: String, userId: Long, user: EditUser) {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.editUser(token, userId, user)
        }

    }

}