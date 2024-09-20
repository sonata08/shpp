package com.example.myprofile.data.datastore.repository.impl

import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.data.datastore.repository.DataStoreRepository
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
): DataStoreRepository {
    override suspend fun saveUserIdTokens(userId: Long, accessToken: String, refreshToken: String) {
        dataStorePreferences.saveUserIdTokens(userId, accessToken, refreshToken)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStorePreferences.saveTokens(accessToken, refreshToken)
    }

    override suspend fun getUserIdTokens() = dataStorePreferences.getUserIdTokens()

    override suspend fun rememberUser(data: Boolean) {
        dataStorePreferences.rememberUser(data)
    }
    override suspend fun getRememberUser(): Boolean = dataStorePreferences.getRememberUser()
    override suspend fun logOut() = dataStorePreferences.clear()
}