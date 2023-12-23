package com.example.myprofile.data.network

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.dto.EditUser
import com.example.myprofile.data.network.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface UserApiService {

    @POST("users")
    suspend fun createUser(
        @Body userCredentialsAuth: UserCredentialsAuth,
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(
        @Body userCredentialsAuth: UserCredentialsAuth,
    ): LoginResponse   // Response<LoginResponse>

    @POST("/users/{userId}")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body user: EditUser
    ): LoginResponse


    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String,
    ): LoginResponse

    @POST("refresh")
    suspend fun refreshToken(
        @Header("RefreshToken") refreshToken: String
    ): String

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
    ): String

}