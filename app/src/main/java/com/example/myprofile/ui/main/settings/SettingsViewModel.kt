package com.example.myprofile.ui.main.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.network.dto.UserIdTokens
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TOKEN = "eyJpdiI6IjVZZ2VuQTlMcTdLeWRJbWE2VXlFSlE9PSIsInZhbHVlIjoic01peCtpZEhqMEk1dmxlVUVKUUdLQ2F6OGNvZDBoNW1xT3RDMDNoMndJaXNqdFg2NG8rUCsxM2RLMVFaZHJhQ214WjgxZW9jcHd5L3orbkdnTnkxaGJ1cHlFdkpsWHBNOXBiOW9ySzB1Tkk9IiwibWFjIjoiOTNlNDEwZjgzY2Y2OTUzZTI3M2U2MjU4MDk1OTEwYjIzZTFmNDI2YmE3YzZlNjQ1OTExMWYyN2QwNDAwN2FmOCJ9"
private const val USER_ID = 552L

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authStateFlow = _authStateFlow.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private var userIdTokens = UserIdTokens()

    fun getUser() {
        viewModelScope.launch {
            userIdTokens = dataStoreRepository.getUserIdTokens()
            Log.d("FAT_ViewModel_gerUser", "getUser: ${userIdTokens.userId}")
            _authStateFlow.value = authRepository.getUser(
                userId = userIdTokens.userId,
                token = userIdTokens.accessToken
            )
            saveUserInViewModel(this)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _authStateFlow.value = authRepository.editUser(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                user = user
            )
            saveUserInViewModel(this)
        }
    }

    private fun saveUserInViewModel(scope: CoroutineScope) {
        scope.launch {
            _authStateFlow.collect {
                if (it is AuthUiState.Success) {
                    _user.value = it.data.data.user
                }
            }
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


}