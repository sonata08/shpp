package com.example.myprofile.data.network.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
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

    private var savedUser = User()

    override suspend fun createUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return try {
            val response = userApiService.createUser(userCredentials)
            Log.d("FAT_AuthRep_create", "response = $response")
            savedUser = response.data.user
            UiState.Success(response.data)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_create", "error = ${e.message}")
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            Log.d("FAT_AuthRep_create_Exc", e.toString())
            UiState.Error(e.message ?: "")
        }
    }

    override suspend fun loginUser(userCredentials: UserCredentials): UiState<LoginResponse> {
        return try {
            val response = userApiService.loginUser(userCredentials)
            savedUser = response.data.user
            UiState.Success(response.data)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string() ?: "createUser ERROR"
            Log.d("FAT_AuthRep_login", "error.message = ${e.message}")
            Log.d("FAT_AuthRep_login", "error = ${error}")
            UiState.Error(e.message())
        }
    }

    override suspend fun getUser(): UiState<User> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        Log.d("FAT_AuthRep_get", "savedUser id = ${savedUser.id}")
        return try {
            if (savedUser.id == -1L) {
                Log.d("FAT_AuthRep_get", "before response")
                val response = userApiService.getUser(
                    userId = userIdTokens.userId,
                    token = userIdTokens.accessToken
                )
                Log.d("FAT_AuthRep_get", "$response")
                savedUser = response.data.user
                UiState.Success(savedUser)
            } else {
                UiState.Success(savedUser)
            }
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_get", "error.message = ${e.message}")
            Log.d("FAT_AuthRep_get", "error = ${error}")
            UiState.Error(getMessageFromHttpException(error))
        }
        catch (e: Exception) {
            Log.d("FAT_AuthRep_get_catch2", "getUser_error = $e")
            UiState.Error(e.message ?: "unknown ERROR")
        }
    }

    override suspend fun editUser(user: User): UiState<User> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        Log.d("FAT_AuthRep_edit", "user id = ${userIdTokens.userId}")
        Log.d("FAT_AuthRep_edit", "user = $user")
        return try {
            val response = userApiService.editUser(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                user = user
            )
            Log.d("FAT_AuthRep_edit", "edit response = $response")
            savedUser = response.data.user
            UiState.Success(savedUser)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_edit_catch", "editUser_error = $error")
            UiState.Error(getMessageFromHttpException(error))
        }
    }

    override fun getSavedUser() = savedUser
}