package com.example.myprofile.data.network.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.response_dto.LoginResponse


interface AuthRepository {

    suspend fun createUser(userCredentials: UserCredentials): UiState<LoginResponse>
    suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse>
    suspend fun getUser(): UiState<User>
    suspend fun editUser(user: User): UiState<User>
    fun getSavedUser(): User
}