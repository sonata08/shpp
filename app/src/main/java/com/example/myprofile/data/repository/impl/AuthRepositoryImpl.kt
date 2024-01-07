package com.example.myprofile.data.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.model.UserInfoHolder
import com.example.myprofile.data.network.UserApiService
import com.example.myprofile.data.network.model.AuthUiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.data.network.model.LoginResponseBase
import com.example.myprofile.data.repository.AuthRepository
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
    private val userInfoHolder: UserInfoHolder,
) : AuthRepository {


    override suspend fun createUser(userCredentials: UserCredentialsAuth): AuthUiState {
        return try {
            val response = userApiService.createUser(userCredentials)
            response.message?.let { Log.d("FAT_AuthRep_create_msg", it) }
            userInfoHolder.user = response.data.user
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
            userInfoHolder.user = response.data.user
            AuthUiState.Success(response)
        } catch (e: HttpException) {
            Log.d("FAT_AuthRep_login_catch", e.toString())
            AuthUiState.Error("")
        }

    }

    override suspend fun getUser(): AuthUiState {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            if (userInfoHolder.user.id == -1L) {
                val response = userApiService.getUser(
                    userId = userIdTokens.userId,
                    token = userIdTokens.accessToken
                )
                Log.d("FAT_AuthRep_getUser", "user id = ${response.data.user.id}")
                userInfoHolder.user = response.data.user
                AuthUiState.Success(response)
            } else {
                // TODO: change to AuthUiStateTest
                val logResponse = LoginResponse(
                    user = userInfoHolder.user,
                    accessToken = userIdTokens.accessToken,
                    refreshToken = userIdTokens.refreshToken
                )
                val tempResponse =
                    LoginResponseBase(
                        status = "Success",
                        code = "200",
                        message = "",
                        data = logResponse
                    )
                AuthUiState.Success(tempResponse)
            }

        } catch (e: Exception) {
            Log.d("FAT_AuthRep_get_catch", "getUser_error = $e")
            AuthUiState.Error(e.message ?: "unknown ERROR")
        }
    }

    override suspend fun editUser(user: User): AuthUiState {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = userApiService.editUser(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                user = user
            )
            userInfoHolder.user = response.data.user
            AuthUiState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            Log.d("FAT_AuthRep_edit_catch", "editUser_error = $error")
            AuthUiState.Error(getMessageFromHttpException(error))
        }
    }

    override fun getSavedUser() = userInfoHolder.user


}