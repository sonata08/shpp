package com.example.myprofile.data.network.api

import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.response_dto.LoginResponse
import com.example.myprofile.data.network.model.response_dto.Tokens
import com.example.myprofile.data.network.model.response_dto.UserGet
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
        @Body userCredentials: UserCredentials,
    ): BaseResponse<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(
        @Body userCredentials: UserCredentials,
    ): BaseResponse<LoginResponse>

    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") userId: Long,
        @Body user: User
    ): BaseResponse<UserGet>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Long,
    ): BaseResponse<UserGet>

    @POST("refresh")
    suspend fun refreshToken(
        @Header("RefreshToken") refreshToken: String
    ): BaseResponse<Tokens>

}