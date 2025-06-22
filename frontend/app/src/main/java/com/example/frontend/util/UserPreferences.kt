package com.example.frontend.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

    object UserPreferences {
    private val Context.dataStore by preferencesDataStore("user_prefs")

    private val MAIL_KEY = stringPreferencesKey("mail") // Thay EMAIL_KEY thành MAIL_KEY
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val REMEMBER_KEY = booleanPreferencesKey("remember")

    suspend fun saveUserData(context: Context, mail: String, password: String, remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MAIL_KEY] = mail
            prefs[PASSWORD_KEY] = password
            prefs[REMEMBER_KEY] = remember
        }
    }

    fun getUserData(context: Context): Flow<Triple<String, String, Boolean>> =
        context.dataStore.data.map { prefs ->
            Triple(
                prefs[MAIL_KEY] ?: "",
                prefs[PASSWORD_KEY] ?: "",
                prefs[REMEMBER_KEY] ?: false
            )
        }

    suspend fun clearUserData(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}