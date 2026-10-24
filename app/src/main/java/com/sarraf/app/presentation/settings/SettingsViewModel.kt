package com.sarraf.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarraf.app.data.preferences.UserPreferencesManager
import com.sarraf.app.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val language: String = "en",
    val themeMode: String = "OLED_DARK", // "SYSTEM", "OLED_DARK", "LIGHT"
    val defaultBaseCurrency: String = "USD",
    val apiKeyMasked: String = "fca_live_••••••••8x9Q",
    val autoRefreshInterval: String = "15M", // "15M", "1H", "MANUAL"
    val cachedRatesCount: Int = 164,
    val cachedSizeKb: Int = 24,
    val lastSyncTime: String = "Today 08:35 PM",
    val pushNotifications: Boolean = true,
    val hapticFeedback: Boolean = true,
    val backgroundSync: Boolean = true,
    val isClearingCache: Boolean = false,
    val isRefreshing: Boolean = false,
    val cacheClearSuccess: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.baseCurrencyFlow.collect { base ->
                _uiState.update { it.copy(defaultBaseCurrency = base) }
            }
        }
        viewModelScope.launch {
            preferencesManager.appThemeFlow.collect { theme ->
                _uiState.update { it.copy(themeMode = theme) }
            }
        }
        viewModelScope.launch {
            preferencesManager.displayLanguageFlow.collect { lang ->
                _uiState.update { it.copy(language = lang) }
            }
        }
        viewModelScope.launch {
            preferencesManager.autoRefreshIntervalFlow.collect { interval ->
                _uiState.update { it.copy(autoRefreshInterval = interval) }
            }
        }
        viewModelScope.launch {
            preferencesManager.pushNotificationsEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(pushNotifications = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesManager.hapticsEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(hapticFeedback = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesManager.backgroundSyncEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(backgroundSync = enabled) }
            }
        }
        viewModelScope.launch {
            currencyRepository.getCachedCountFlow().collect { count ->
                if (count > 0) {
                    _uiState.update { it.copy(cachedRatesCount = count, cachedSizeKb = (count * 0.15).toInt().coerceAtLeast(12)) }
                }
            }
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferencesManager.setDisplayLanguage(lang)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.setAppTheme(theme)
        }
    }

    fun setDefaultBaseCurrency(base: String) {
        viewModelScope.launch {
            preferencesManager.setBaseCurrency(base)
        }
    }

    fun setAutoRefreshInterval(interval: String) {
        viewModelScope.launch {
            preferencesManager.setAutoRefreshInterval(interval)
        }
    }

    fun togglePushNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setPushNotificationsEnabled(enabled)
        }
    }

    fun toggleHaptics(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setHapticsEnabled(enabled)
        }
    }

    fun toggleBackgroundSync(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBackgroundSyncEnabled(enabled)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            currencyRepository.refreshRates(_uiState.value.defaultBaseCurrency)
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    lastSyncTime = "Just now"
                )
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearingCache = true) }
            currencyRepository.clearCache()
            kotlinx.coroutines.delay(600)
            _uiState.update {
                it.copy(
                    isClearingCache = false,
                    cachedRatesCount = 0,
                    cachedSizeKb = 0,
                    cacheClearSuccess = true
                )
            }
            kotlinx.coroutines.delay(2000)
            _uiState.update { it.copy(cacheClearSuccess = false) }
        }
    }
}
