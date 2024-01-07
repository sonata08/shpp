package com.example.myprofile.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.utils.Parser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
The class is responsible for storing user's data
 */
@Singleton
class DataStorePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {


    suspend fun saveUserIdTokens(userId: Long, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = "Bearer $accessToken"
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[USER_ID] = userId
        }
    }

    suspend fun getUserIdTokens(): UserIdTokens {
        return try {
            val preferences = this.dataStore.data.first()
            val accessToken = preferences[ACCESS_TOKEN] ?: ""
            val refreshToken = preferences[REFRESH_TOKEN] ?: ""
            val userId = preferences[USER_ID] ?: -1
            UserIdTokens(userId = userId, accessToken = accessToken, refreshToken = refreshToken)
        } catch (e: NoSuchElementException) {
            UserIdTokens()
        }
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


    suspend fun saveName(email: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = Parser.parseEmail(email)
        }
    }

    /**
    Gets data from dataStore or an empty string if there is now data.
    Throws an error if smth goes wrong.
    Returns an instance of UserCredentials.
     */
    val getCredentialsFlow: Flow<UserCredentials> = this.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val email = preferences[EMAIL_KEY] ?: ""
            val password = preferences[PASSWORD_KEY] ?: ""
            val name = preferences[NAME_KEY] ?: ""
            UserCredentials(email, password, name)
        }

    suspend fun saveCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[PASSWORD_KEY] = password
        }
    }
    // try {
    //    dataStore.edit { preferences ->
    //      preferences[EMAIL_KEY] = email
    //      preferences[PASSWORD_KEY] = password
    //    }
    //  } catch (e: IOException) {
    //    // Handle error
    //  }

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = longPreferencesKey("user_id")
        private val REMEMBER_USER = booleanPreferencesKey("remember_user")
    }
}