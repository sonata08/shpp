package com.example.myprofile.data.datastore.repository

import com.example.myprofile.data.network.model.response_dto.Tokens


interface DataStoreRepository {

    suspend fun saveUserId(userId: Long)
    suspend fun getUserId(): Long

    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getTokens(): Tokens

    suspend fun rememberUser(data: Boolean)
    suspend fun getRememberUser(): Boolean
    suspend fun logOut()
}