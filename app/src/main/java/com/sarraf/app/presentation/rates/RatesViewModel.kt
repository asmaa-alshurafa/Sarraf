package com.sarraf.app.presentation.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarraf.app.data.preferences.UserPreferencesManager
import com.sarraf.app.domain.model.CurrencyCategory
import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.domain.usecase.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RatesUiState(
    val baseCurrency: String = "USD",
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isAutoSyncActive: Boolean = true,
    val errorMessage: String? = null,
    val allRates: List<CurrencyRate> = emptyList(),
    val filteredRates: List<CurrencyRate> = emptyList(),
    val watchlistRates: List<CurrencyRate> = emptyList(),
    val regionalAndGlobalRates: List<CurrencyRate> = emptyList()
)

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatesUiState())
    val uiState: StateFlow<RatesUiState> = _uiState.asStateFlow()

    private var autoSyncJob: Job? = null

    val baseCurrencies = listOf(
        BaseCurrencyOption("USD", "($)", "🇺🇸"),
        BaseCurrencyOption("EUR", "(€)", "🇪🇺"),
        BaseCurrencyOption("ILS", "(₪)", "🇵🇸"),
        BaseCurrencyOption("JOD", "(د.أ)", "🇯🇴")
    )

    init {
        viewModelScope.launch {
            preferencesManager.baseCurrencyFlow.collect { base ->
                _uiState.update { it.copy(baseCurrency = base) }
                observeRatesForBase(base)
            }
        }
        startAutoSyncTicker()
    }

    private fun observeRatesForBase(base: String) {
        viewModelScope.launch {
            getExchangeRatesUseCase(base).collect { rates ->
                val query = _uiState.value.searchQuery.trim()
                val filtered = filterRatesByQuery(rates, query)
                val watchlist = rates.filter { it.category == CurrencyCategory.PRIMARY }.take(4)
                val regionalGlobal = filtered.filter { it.category != CurrencyCategory.PRIMARY || query.isNotBlank() }

                _uiState.update { current ->
                    current.copy(
                        allRates = rates,
                        filteredRates = filtered,
                        watchlistRates = watchlist,
                        regionalAndGlobalRates = regionalGlobal
                    )
                }
            }
        }
    }

    fun setBaseCurrency(base: String) {
        viewModelScope.launch {
            preferencesManager.setBaseCurrency(base)
            refreshRates()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { current ->
            val trimmed = query.trim()
            val filtered = filterRatesByQuery(current.allRates, trimmed)
            val regionalGlobal = if (trimmed.isBlank()) {
                current.allRates.filter { it.category != CurrencyCategory.PRIMARY }
            } else {
                filtered
            }
            current.copy(
                searchQuery = query,
                filteredRates = filtered,
                regionalAndGlobalRates = regionalGlobal
            )
        }
    }

    fun clearSearch() {
        onSearchQueryChanged("")
    }

    private fun filterRatesByQuery(rates: List<CurrencyRate>, query: String): List<CurrencyRate> {
        if (query.isBlank()) return rates
        return rates.filter { rate ->
            rate.code.contains(query, ignoreCase = true) ||
            rate.name.contains(query, ignoreCase = true) ||
            rate.subtitle.contains(query, ignoreCase = true)
        }
    }

    fun refreshRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val base = _uiState.value.baseCurrency
            val result = getExchangeRatesUseCase.refresh(base)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = if (result.isFailure) result.exceptionOrNull()?.message else null
                )
            }
        }
    }

    private fun startAutoSyncTicker() {
        autoSyncJob?.cancel()
        autoSyncJob = viewModelScope.launch {
            while (isActive) {
                delay(3000L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoSyncJob?.cancel()
    }
}

data class BaseCurrencyOption(
    val code: String,
    val symbolFormatted: String,
    val flag: String
)
