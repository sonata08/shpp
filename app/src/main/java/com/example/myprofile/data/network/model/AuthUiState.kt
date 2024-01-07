package com.example.myprofile.data.network.model

sealed class AuthUiState {
    data class Success(val data: LoginResponseBase) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    data object Loading : AuthUiState()
    data object Initial : AuthUiState()
}


sealed class AuthUiStateTest<out T> {
    data class Success<out T>(val data: T) : AuthUiStateTest<T>()
    data class Error(val message: String) : AuthUiStateTest<Nothing>()
    data object Loading : AuthUiStateTest<Nothing>()
    data object Initial : AuthUiStateTest<Nothing>()
}