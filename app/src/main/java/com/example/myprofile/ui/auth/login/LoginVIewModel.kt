package com.example.myprofile.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.network.dto.LoginResponseData
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TOKEN = "eyJpdiI6Iml2SW5XMHYrMEdHWkNFNjJKV2J0QkE9PSIsInZhbHVlIjoiU3dPRHgreE9tanRFc2wxM2RmUGszYXUwTkVXbTA1YjhiUGdrL3JQeDEzM2k1dDJ2R29lekhIWW4rTjZOZDFXMVRDQXZKc054VFRSZmpIUDZ2ZmJBOXBHcUVZM3dJWE01QUJEc2ltY3pWS1U9IiwibWFjIjoiZTQ4ZGRlM2RmNTk3NzI3YTcxZmIxMGVkYjEwOWY0Y2VmZDY3ZDU5NDY1OGM2YzQzNTYyZDRiYWUyZTFkMTdlMiJ9"
private const val REFRESH_TOKEN = "eyJpdiI6ImdKdW1yMnB0bjZ1N1kyUE04SXlpL0E9PSIsInZhbHVlIjoiU0RtTGdFM0E0a2l5UndxZ3kxWmViU2FXbWx4OUVyREYvc2NMbWpQS3hhM0xTKzUzSmYyMVRqc2szTWI3Nmp1Q2hCOWNkbzI4Q1JoZENTTVQrMVB1bHo3RE1Eek0xbVZKWTF2NlJSYWdRMlU9IiwibWFjIjoiNGI0MjZmNmMxM2QyMDkzMWUxNDZhMTQ3N2M5OWE1MDcwNWExMWE1YjI1YjVjN2I0MjkzYWNmZDdkZWRmMjYzOCJ9"
private const val USER_ID = 552
private const val EMAIL = "t@mail.com"
private const val PASSWORD = "123"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

    fun loginUser(userCredentials: UserCredentialsAuth) {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.loginUser(userCredentials)
        }
    }

    fun saveUserIdTokens(data: LoginResponseData) {
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
                _authStateFlow.value = authRepository.getUser(userId = userIdTokens.userId, token = userIdTokens.accessToken)

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