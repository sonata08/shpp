package com.example.myprofile

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class DataStorePreferences(private val dataStore: DataStore<Preferences>) {

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
            preferences[NAME_KEY] = parseEmail(email)
            Log.d("##DataStore", preferences[NAME_KEY].toString())
        }
    }

    private fun parseEmail(email: String): String {
        val emailName = email.substringBefore("@")
        return if (emailName.contains(".")) {
            val name = emailName
                .split(".")
                .joinToString(separator = " ") { it -> it.replaceFirstChar { it.uppercase() } }
            name
        } else {
            emailName
        }
    }


    companion object {
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")
        private val NAME_KEY = stringPreferencesKey("user_name")
    }

}