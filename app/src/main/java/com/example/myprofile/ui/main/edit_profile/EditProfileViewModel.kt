package com.example.myprofile.ui.main.edit_profile

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
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<UiState<User>>(UiState.Initial)
    val authStateFlow = _authStateFlow.asStateFlow()


    fun getUser() = authRepository.getSavedUser()

    fun updateUser(user: User) {
        viewModelScope.launch {
            _authStateFlow.value = UiState.Loading
            _authStateFlow.value = authRepository.editUser(user)
            Log.d("FAT_EditProf_VM", "user updated")
        }
    }
}