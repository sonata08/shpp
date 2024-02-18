package com.example.myprofile.utils.extentions

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val DATA_STORE_NAME = "settings"

// DataStore property extension for easier access
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

/**
 * Extension function to show a short duration Toast message.
 *
 * @param message The message to be displayed in the Toast.
 */
fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}