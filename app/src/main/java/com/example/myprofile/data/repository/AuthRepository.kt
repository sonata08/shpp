package com.example.myprofile.data.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.model.AuthUiState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    suspend fun createUser(userCredentials: UserCredentialsAuth): AuthUiState
    suspend fun loginUser(userCredentials: UserCredentialsAuth): AuthUiState
    suspend fun getUser(): AuthUiState
    suspend fun editUser(user: User): AuthUiState
    fun getSavedUser(): User
}