package com.example.myprofile.ui.main.edit_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.AuthUiState
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val authStateFlow = _authStateFlow.asStateFlow()


    fun getUser() = authRepository.getSavedUser()

    fun updateUser(user: User) {
        viewModelScope.launch {
            _authStateFlow.value = AuthUiState.Loading
            delay(1000)
            _authStateFlow.value = authRepository.editUser(user)
            Log.d("FAT_EditProf_VM", "user updated")
        }
    }
}