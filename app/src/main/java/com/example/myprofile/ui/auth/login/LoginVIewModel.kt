package com.example.myprofile.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.model.AuthUiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject



@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val authStateFlow = _authStateFlow.asStateFlow()

    fun loginUser(userCredentials: UserCredentialsAuth) {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.loginUser(userCredentials)
        }
    }

    fun saveUserIdTokens(data: LoginResponse) {
        viewModelScope.launch {
            dataStoreRepository.saveUserIdTokens(data.user.id, data.accessToken, data.refreshToken)
        }
    }

    private val _tokensStatus = MutableStateFlow(false)
    val tokensStatus: StateFlow<Boolean>
        get() = _tokensStatus

    fun isTokenValid() {
        viewModelScope.launch {
            val tokens = dataStoreRepository.getUserIdTokens()
            try {
                // TODO: authRepository.getUser(userId) and login user
            } catch (e: Exception) {
                // TODO: ask user to login
            }
        }

    }

    fun rememberUser(data: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.rememberUser(data)
        }
    }


    fun autoLoginUser() {
        Log.d("FAT_ViewModel_auto", "autoLoginUser")
        viewModelScope.launch {
            if (dataStoreRepository.getRememberUser()) {
                Log.d("FAT_ViewModel_auto", "remember user")
                val userIdTokens = dataStoreRepository.getUserIdTokens()
                try {
                    _authStateFlow.value = authRepository.getUser()
                } catch (e: HttpException) {
                    Log.d("FAT_ViewModel_auto", "HttpException auto login")
                }

            }
        }
    }



    fun refreshToken() {
        viewModelScope.launch {
            try {
//                _userFlow.value = authRepository.refreshToken(REFRESH_TOKEN)
//                Log.d("FAT_ViewModel", "refresh: $user")
            } catch (e: Exception) {
                Log.d("FAT_ViewModel_error", e.toString())
            }

        }
    }






}