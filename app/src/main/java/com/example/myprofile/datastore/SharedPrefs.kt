package com.example.myprofile.datastore

import android.content.Context
import android.content.SharedPreferences

/*
    Stores data to SharedPreferences. Not currently in use.
 */
class SharedPrefs(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)

    var email: String
        get() = preferences.getString(EMAIL, "") ?: ""
        set(value) = preferences.edit().putString(EMAIL, value).apply()

    var password: String
        get() = preferences.getString(PASSWORD, "") ?: ""
        set(value) = preferences.edit().putString(PASSWORD, value).apply()

    companion object {
        private const val MY_PREFS = "com.example.myprofile.PREFERENCES"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}