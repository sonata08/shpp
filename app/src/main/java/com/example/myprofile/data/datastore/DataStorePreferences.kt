package com.example.myprofile.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myprofile.data.network.INVALID_ID
import com.example.myprofile.data.network.model.response_dto.Tokens
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
The class is responsible for storing user's data
 */
class DataStorePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    
    suspend fun saveUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun getUserId(): Long {
        val preferences = this.dataStore.data.first()
        return preferences[USER_ID] ?: INVALID_ID
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun getTokens(): Tokens {
        val preferences = this.dataStore.data.first()
        val accessToken = preferences[ACCESS_TOKEN] ?: ""
        val refreshToken = preferences[REFRESH_TOKEN] ?: ""
        return Tokens(accessToken, refreshToken)
    }

    suspend fun rememberUser(data: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_USER] = data
        }
    }

    suspend fun getRememberUser(): Boolean {
        val preferences = this.dataStore.data.first()
        return preferences[REMEMBER_USER] ?: false
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {

        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = longPreferencesKey("user_id")
        private val REMEMBER_USER = booleanPreferencesKey("remember_user")
    }
}