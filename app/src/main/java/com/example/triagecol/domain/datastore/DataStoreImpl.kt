package com.example.triagecol.domain.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.triagecol.domain.UserPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreImpl constructor(
    private val appContext: Context
) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val SAVE_LOGIN = stringPreferencesKey("save_login")

    val readSaveLogin: Flow<String> = appContext.dataStore.data
        .map { preferences ->
            preferences[SAVE_LOGIN] ?: UserPage.LOGIN.toString()
        }
    suspend fun writeSaveLogin(userPage: UserPage) {
        appContext.dataStore.edit { settings ->
            settings[SAVE_LOGIN] = userPage.toString()
        }
    }

    private val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
    val exampleCounterFlow: Flow<Int> = appContext.dataStore.data
        .map { preferences ->
            preferences[EXAMPLE_COUNTER] ?: 0
        }
    suspend fun incrementCounter() {
        appContext.dataStore.edit { settings ->
            val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
            settings[EXAMPLE_COUNTER] = currentCounterValue + 1
        }
    }
}