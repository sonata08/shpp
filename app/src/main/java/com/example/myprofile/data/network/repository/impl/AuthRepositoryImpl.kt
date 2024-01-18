package com.example.myprofile.data.network.repository.impl

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.UNKNOWN_ERROR
import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.network.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import com.example.myprofile.utils.getMessageFromHttpException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val dataStoreRepository: DataStoreRepository,
) : AuthRepository {

    // current user logged in
    private var savedUser = User()

    override suspend fun createUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return try {
            val response = userApiService.createUser(userCredentials)
            savedUser = response.data.user
            UiState.Success(response.data)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return try {
            val response = userApiService.loginUser(userCredentials)
            savedUser = response.data.user
            UiState.Success(response.data)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun getUser(): UiState<User> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            // if user is not saved -> get user from server
            if (savedUser.id == -1L) {
                val response = userApiService.getUser(
                    userId = userIdTokens.userId,
                    token = userIdTokens.accessToken
                )
                savedUser = response.data.user
                UiState.Success(savedUser)
            } else {
                UiState.Success(savedUser)
            }
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun editUser(user: User): UiState<User> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = userApiService.editUser(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                user = user
            )
            savedUser = response.data.user
            UiState.Success(savedUser)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override fun getSavedUser() = savedUser
}