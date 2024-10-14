package com.example.myprofile.data.datastore.repository.impl

import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.data.datastore.repository.DataStoreRepository
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
): DataStoreRepository {
    override suspend fun saveUserId(userId: Long) {
        dataStorePreferences.saveUserId(userId)
    }

    override suspend fun getUserId() = dataStorePreferences.getUserId()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStorePreferences.saveTokens(accessToken, refreshToken)
    }

    override suspend fun getTokens() = dataStorePreferences.getTokens()

    override suspend fun rememberUser(data: Boolean) {
        dataStorePreferences.rememberUser(data)
    }
    override suspend fun getRememberUser(): Boolean = dataStorePreferences.getRememberUser()
    override suspend fun logOut() = dataStorePreferences.clear()
}