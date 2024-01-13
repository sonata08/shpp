package com.example.myprofile.data.network.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.network.model.Users

interface AuthRepository {

    suspend fun createUser(userCredentials: UserCredentials): UiState<LoginResponse>
    suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse>
    suspend fun getUser(): UiState<Users>
    suspend fun editUser(user: User): UiState<Users>
    fun getSavedUser(): User
}