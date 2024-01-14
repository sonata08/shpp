package com.example.myprofile.data.network.repository.impl

import android.util.Log
import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.repository.TokenManager
import com.example.myprofile.data.repository.DataStoreRepository
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenManagerImpl @Inject constructor(
    // to avoid dependency cycle -> Provider<T> + .get()
    // This means TokenManagerImpl will be created before UserApiService is created
    // and later on it will receive singleton UserApiService instance it needs.
    private val userApiService: Provider<UserApiService>,
    private val dataStoreRepository: DataStoreRepository,
): TokenManager {
    override suspend fun refreshTokens(): String? {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = userApiService.get().refreshToken(
                userIdTokens.refreshToken
            )
            dataStoreRepository.saveTokens(response.data.accessToken, response.data.refreshToken)
            Log.d("FAT_TokenManager", "refreshToken DONE")
            response.data.accessToken
        } catch (e: Exception) {
            Log.d("FAT_TokenManager_catch", "refreshToken ERROR")
            null
        }
    }
}