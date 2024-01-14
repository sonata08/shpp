package com.example.myprofile.ui.main.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<UiState<User>>(UiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()


    fun getUser() {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.getUser()
        }
    }
}