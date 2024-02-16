package com.example.myprofile.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.utils.Parser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
The class is responsible for storing user's data
 */
class DataStorePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    /**
    Gets data from dataStore or an empty string if there is now data.
    Throws an error if smth goes wrong.
    Returns an instance of UserCredentials.
     */
    val getCredentialsFlow: Flow<UserCredentials> = dataStore.data
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

    suspend fun saveName(email: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = Parser.parseEmail(email)
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")
        private val NAME_KEY = stringPreferencesKey("user_name")
    }
}