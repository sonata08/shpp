package com.example.myprofile.ui.main.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.Users
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

    private val _authStateFlow = MutableStateFlow<UiState<Users>>(UiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

//    private val _user = MutableStateFlow(authRepository.getUserFlow())
//    val user = _user.asStateFlow()

//    val user = userInfoHolder.getUserFlow()

    fun getUser() {
        viewModelScope.launch {
            Log.d("FAT_ViewModel_gerUser", "GETTING USER...")
            _authStateFlow.value = authRepository.getUser()
        }
    }

//    fun updateUser(user: User) {
//        viewModelScope.launch {
//            _authStateFlow.value = authRepository.editUser(
//                token = userIdTokens.accessToken,
//                userId = userIdTokens.userId,
//                user = user
//            )
//        }
//    }


//    fun autoLoginUser() {
//        Log.d("FAT_ViewModel_auto", "autoLoginUser")
//        viewModelScope.launch {
//            if (dataStoreRepository.getRememberUser()) {
//                Log.d("FAT_ViewModel_auto", "remember user")
//                val userIdTokens = dataStoreRepository.getUserIdTokens()
//                _authStateFlow.value = authRepository.getUser(
//                    userId = userIdTokens.userId,
//                    token = userIdTokens.accessToken
//                )
//
//            }
//        }
//    }


}