package com.example.frontend.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

// Sử dụng tên khác cho dataStore
val Context.tokenDataStore by preferencesDataStore("token_prefs")

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val TOKEN_KEY = stringPreferencesKey("access_token")

    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences[TOKEN_KEY]
    }

    suspend fun clearToken() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}