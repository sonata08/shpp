package com.example.myprofile.data.network

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.model.LoginResponseBase
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserApiService {

    @POST("users")
    suspend fun createUser(
        @Body userCredentialsAuth: UserCredentialsAuth,
    ): LoginResponseBase

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(
        @Body userCredentialsAuth: UserCredentialsAuth,
    ): LoginResponseBase   // Response<LoginResponseBase>

    @PUT("users/{userId}")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body user: User
    ): LoginResponseBase


    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String,
    ): LoginResponseBase

    @POST("refresh")
    suspend fun refreshToken(
        @Header("RefreshToken") refreshToken: String
    ): String

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
    ): String

}