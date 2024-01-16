package com.example.myprofile.ui.auth.singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.network.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _authStateFlow = MutableStateFlow<UiState<LoginResponse>>(UiState.Initial)
    val authStateFlow = _authStateFlow.asStateFlow()


    fun createUser(userCredentials: UserCredentials) {
        _authStateFlow.value = UiState.Initial
        viewModelScope.launch {
            _authStateFlow.value = authRepository.createUser(userCredentials)
        }
    }

    fun saveUserIdTokens(data: LoginResponse) {
        viewModelScope.launch {
            dataStoreRepository.saveUserIdTokens(data.user.id, data.accessToken, data.refreshToken)
        }
    }

    fun rememberUser(data: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.rememberUser(data)
        }
    }

    fun loginUserTEST(userCredentials: UserCredentials) {
        _authStateFlow.value = UiState.Loading
        viewModelScope.launch {
            _authStateFlow.value = authRepository.loginUser(userCredentials)
        }
    }
}