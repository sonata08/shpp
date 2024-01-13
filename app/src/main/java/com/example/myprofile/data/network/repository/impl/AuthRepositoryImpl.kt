package com.example.myprofile.data.network.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.network.model.Users
import com.example.myprofile.data.network.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import com.example.myprofile.utils.getMessageFromHttpException
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val dataStoreRepository: DataStoreRepository,
) : AuthRepository {

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
            Log.d("FAT_AuthRep_create_Exc", e.toString())
            UiState.Error("")
        }
    }

    override suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return try {
            val response = userApiService.loginUser(userCredentials)
            Log.d("AuthRepository", "response1: $response")
            savedUser = response.data.user
            UiState.Success(response.data)
        } catch (e: HttpException) {
            Log.d("FAT_AuthRep_login_catch", e.toString())
            UiState.Error("")
        }

    }

    override suspend fun getUser(): UiState<Users> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            if (savedUser.id == -1L) {
                val response = userApiService.getUser(
                    userId = userIdTokens.userId,
                    token = userIdTokens.accessToken
                )
                savedUser = response.data.users.first()
                UiState.Success(response.data)
            } else {
                UiState.Success(Users(users = emptyList()))
            }

        } catch (e: Exception) {
            Log.d("FAT_AuthRep_get_catch", "getUser_error = $e")
            UiState.Error(e.message ?: "unknown ERROR")
        }
    }

    override suspend fun editUser(user: User): UiState<Users> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = userApiService.editUser(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                user = user
            )
            savedUser = response.data.users.first()
            UiState.Success(response.data)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_edit_catch", "editUser_error = $error")
            UiState.Error(getMessageFromHttpException(error))
        }
    }

    override fun getSavedUser() = savedUser
}