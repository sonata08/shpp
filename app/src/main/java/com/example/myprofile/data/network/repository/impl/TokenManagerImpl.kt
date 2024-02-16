package com.example.myprofile.data.network.repository.impl

import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.repository.TokenManager
import com.example.myprofile.data.repository.DataStoreRepository
import javax.inject.Inject
import javax.inject.Provider

/**
 * Implementation of the [TokenManager] interface responsible for refreshing user tokens.
 *
 * @property userApiService The API service used to interact with the user-related endpoints.
 * @property dataStoreRepository The repository responsible for managing token data in the data store.
 */
class TokenManagerImpl @Inject constructor(
    // to avoid dependency cycle -> Provider<T> + .get()
    // This means TokenManagerImpl will be created before UserApiService is created
    // and later on it will receive singleton UserApiService instance it needs.
    private val userApiService: Provider<UserApiService>,
    private val dataStoreRepository: DataStoreRepository,
): TokenManager {
    override suspend fun refreshTokens(): String? {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        if (userIdTokens.refreshToken.isEmpty()) return null
        return try {
            val response = userApiService.get().refreshToken(
                userIdTokens.refreshToken
            )
            dataStoreRepository.saveTokens(response.data.accessToken, response.data.refreshToken)
            response.data.accessToken
        } catch (e: Exception) {
            null
        }
    }
}