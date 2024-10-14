package com.example.myprofile.data.network.repository.impl

import com.example.myprofile.data.datastore.repository.DataStoreRepository
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.handleApiCall
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.response_dto.LoginResponse
import com.example.myprofile.data.network.repository.AuthRepository
import com.example.myprofile.data.room.repository.DatabaseRepository
import com.example.myprofile.utils.extentions.isInvalidId
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val database: DatabaseRepository,
) : AuthRepository {

    // current user logged in
    private var savedUser = User()

    override suspend fun createUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return handleApiCall(
            onApiCall = {
                val response = userApiService.createUser(userCredentials)
                savedUser = response.data.user
                database.insertUser(response.data.user)
                UiState.Success(response.data)
            }
        )
    }

    override suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return handleApiCall(
            onApiCall = {
                val response = userApiService.loginUser(userCredentials)
                savedUser = response.data.user
                database.insertUser(response.data.user)
                UiState.Success(response.data)
            }
        )
    }

    override suspend fun getUser(): UiState<User> {
        val userId = dataStoreRepository.getUserId()
        return handleApiCall(
            onApiCall = {
                // if user is not saved -> get user from server
                if (savedUser.id.isInvalidId()) {
                    val response = userApiService.getUser(
                        userId = userId,
                    )
                    savedUser = response.data.user
                    UiState.Success(savedUser)
                } else {
                    UiState.Success(savedUser)
                }
            },
            onConnectException = {
                val user = database.findUserById(userId)
                UiState.Success(user)
            }
        )
    }

    override suspend fun editUser(user: User): UiState<User> {
        val userId = dataStoreRepository.getUserId()
        return handleApiCall(
            onApiCall = {
                val response = userApiService.editUser(
                    userId = userId,
                    user = user
                )
                savedUser = response.data.user
                UiState.Success(savedUser)
            }
        )
    }

    override fun getSavedUser() = savedUser
}