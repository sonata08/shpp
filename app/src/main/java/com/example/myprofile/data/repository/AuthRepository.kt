package com.example.myprofile.data.repository

import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.network.dto.EditUser

interface AuthRepository {

    suspend fun createUser(userCredentials: UserCredentialsAuth): AuthUiState
    suspend fun loginUser(userCredentials: UserCredentialsAuth): AuthUiState
    suspend fun getUser(userId: Long, token: String): AuthUiState
    suspend fun editUser(token: String, userId: Long, user: EditUser): AuthUiState


}