package com.example.frontend.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Sử dụng tên khác cho dataStore
val Context.userDataStore by preferencesDataStore("user_prefs")

object UserPreferences {
    private val MAIL_KEY = stringPreferencesKey("mail")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val REMEMBER_KEY = booleanPreferencesKey("remember")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    suspend fun saveUserData(context: Context, mail: String, password: String, remember: Boolean) {
        context.userDataStore.edit { prefs ->
            prefs[MAIL_KEY] = mail
            prefs[PASSWORD_KEY] = password
            prefs[REMEMBER_KEY] = remember
            prefs[IS_LOGGED_IN] = true
        }
    }

    fun getUserData(context: Context): Flow<Triple<String, String, Boolean>> =
        context.userDataStore.data.map { prefs ->
            Triple(
                prefs[MAIL_KEY] ?: "",
                prefs[PASSWORD_KEY] ?: "",
                prefs[REMEMBER_KEY] ?: false
            )
        }

    suspend fun clearUserData(context: Context) {
        context.userDataStore.edit { prefs ->
            prefs.clear()
        }
    }

    fun checkLoggedIn(context: Context): Flow<Boolean> =
        context.userDataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN] ?: false
        }
}