package com.example.myprofile.data.repository.impl

import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.data.repository.DataStoreRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
): DataStoreRepository {
    override suspend fun saveUserIdTokens(userId: Long, accessToken: String, refreshToken: String) {
        dataStorePreferences.saveUserIdTokens(userId, accessToken, refreshToken)
    }
    override suspend fun getUserIdTokens() = dataStorePreferences.getUserIdTokens()

    override suspend fun rememberUser(data: Boolean) {
        dataStorePreferences.rememberUser(data)
    }
    override suspend fun getRememberUser(): Boolean = dataStorePreferences.getRememberUser()
}