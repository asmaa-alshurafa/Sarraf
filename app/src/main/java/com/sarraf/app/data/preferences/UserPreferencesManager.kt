package com.sarraf.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sarraf_user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val BASE_CURRENCY = stringPreferencesKey("base_currency")
        val APP_THEME = stringPreferencesKey("app_theme") // "SYSTEM", "OLED_DARK", "LIGHT"
        val API_KEY = stringPreferencesKey("custom_api_key")
        val AUTO_REFRESH_INTERVAL = stringPreferencesKey("auto_refresh_interval") // "15M", "1H", "MANUAL"
        val PUSH_NOTIFICATIONS_ENABLED = booleanPreferencesKey("push_notifications_enabled")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val BACKGROUND_SYNC_ENABLED = booleanPreferencesKey("background_sync_enabled")
        val DISPLAY_LANGUAGE = stringPreferencesKey("display_language") // "en", "ar"
    }

    val baseCurrencyFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.BASE_CURRENCY] ?: "USD"
        }

    val appThemeFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.APP_THEME] ?: "OLED_DARK"
        }

    val apiKeyFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.API_KEY] ?: ""
        }

    val autoRefreshIntervalFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.AUTO_REFRESH_INTERVAL] ?: "15M"
        }

    val pushNotificationsEnabledFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.PUSH_NOTIFICATIONS_ENABLED] ?: true
        }

    val hapticsEnabledFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.HAPTICS_ENABLED] ?: true
        }

    val backgroundSyncEnabledFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.BACKGROUND_SYNC_ENABLED] ?: true
        }

    val displayLanguageFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.DISPLAY_LANGUAGE] ?: "en"
        }

    suspend fun setBaseCurrency(baseCurrency: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BASE_CURRENCY] = baseCurrency
        }
    }

    suspend fun setAppTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_THEME] = theme
        }
    }

    suspend fun setApiKey(apiKey: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_KEY] = apiKey
        }
    }

    suspend fun setAutoRefreshInterval(interval: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_REFRESH_INTERVAL] = interval
        }
    }

    suspend fun setPushNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PUSH_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun setBackgroundSyncEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BACKGROUND_SYNC_ENABLED] = enabled
        }
    }

    suspend fun setDisplayLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISPLAY_LANGUAGE] = lang
        }
    }
}
