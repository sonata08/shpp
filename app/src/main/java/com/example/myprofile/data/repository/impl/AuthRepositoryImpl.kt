package com.example.myprofile.data.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.UserApiService
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.utils.getMessageFromHttpException
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : AuthRepository {
    override suspend fun createUser(userCredentials: UserCredentialsAuth): AuthUiState {
        return try {
            val response = userApiService.createUser(userCredentials)
            response.message?.let { Log.d("FAT_AuthRep_create_msg", it) }
            if (response.message.isNullOrEmpty()) {
                AuthUiState.Success(response)
                Log.d(
                    "FAT_AuthRep_createUser",
                    "user id = ${response.data.user.id}"
                )
            }
            AuthUiState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            AuthUiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            Log.d("FAT_AuthRep_create_Exc", e.toString())
            AuthUiState.Error("")
        }
    }

    override suspend fun loginUser(userCredentials: UserCredentialsAuth): AuthUiState {
        return try {
            val response = userApiService.loginUser(userCredentials)
            Log.d("AuthRepository", "response1: $response")
            AuthUiState.Success(response)
        } catch (e: HttpException) {
            Log.d("FAT_AuthRep_login_catch", e.toString())
            AuthUiState.Error("")
        }

    }

    override suspend fun getUser(userId: Long, token: String): AuthUiState {
        return try {
            // TODO: remove token2
            val token2 = "Bearer $token"
            val response = userApiService.getUser(userId = userId, token = token2)
            Log.d("FAT_AuthRep_get", response.toString())
            AuthUiState.Success(response)
        } catch (e: Exception) {
            Log.d("FAT_AuthRep_get_catch", "getUser_error = $e")
            AuthUiState.Error(e.message ?: "unknown ERROR")
        }
    }

    override suspend fun editUser(token: String, userId: Long, user: User): AuthUiState {
        return try {
            val token2 = "Bearer $token"
            val response = userApiService.editUser(token = token, userId = userId, user = user)
            Log.d("FAT_AuthRep_edit", response.toString())
            AuthUiState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_edit_catch", "editUser_error = $error")
            AuthUiState.Error(getMessageFromHttpException(error))
        }
    }


}