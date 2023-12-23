package com.example.myprofile.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
): ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            val userIdTokens = dataStoreRepository.getUserIdTokens()
            _authStateFlow.value = authRepository.getUser(
                userId = userIdTokens.userId,
                token = userIdTokens.accessToken
            )
        }
    }

}