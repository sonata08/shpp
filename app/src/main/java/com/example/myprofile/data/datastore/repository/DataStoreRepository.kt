package com.example.myprofile.data.datastore.repository

import com.example.myprofile.data.network.model.UserIdTokens


interface DataStoreRepository {

    suspend fun saveUserIdTokens(userId: Long, accessToken: String, refreshToken: String)
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getUserIdTokens(): UserIdTokens
    suspend fun rememberUser(data: Boolean)
    suspend fun getRememberUser(): Boolean
    suspend fun logOut()
}