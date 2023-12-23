package com.example.myprofile.data.network.dto

sealed class AuthUiState {
    data class Success(val data: LoginResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    data object Loading : AuthUiState()
}