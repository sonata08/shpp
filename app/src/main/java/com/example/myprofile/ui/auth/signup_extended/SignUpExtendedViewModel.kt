package com.example.myprofile.ui.auth.signup_extended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<UiState<User>>(UiState.Initial)
    val authStateFlow = _authStateFlow.asStateFlow()

    private var _photoUri = MutableStateFlow("")
    val photoUri = _photoUri.asStateFlow()

    fun editUser(user: User) {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.editUser(user)
        }
    }

    fun setPhotoUri(uri: String) {
        _photoUri.value = uri
    }
}