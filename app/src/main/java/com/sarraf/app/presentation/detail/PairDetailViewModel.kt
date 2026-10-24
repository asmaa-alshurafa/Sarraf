package com.sarraf.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.domain.usecase.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PairDetailUiState(
    val baseCurrency: String = "USD",
    val targetCurrency: String = "ILS",
    val spotRate: Double = 3.6845,
    val changePercent24h: Double = 0.42,
    val changeAbsolute24h: Double = 0.0154,
    val low24h: Double = 3.6680,
    val high24h: Double = 3.6910,
    val selectedTimeframe: String = "1W",
    val isFavorite: Boolean = false,
    val isAlertSet: Boolean = false,
    val chartDataPoints: List<Float> = listOf(
        3.6680f, 3.6710f, 3.6695f, 3.6740f, 3.6780f, 3.6810f, 3.6845f
    ),
    val volume24h: String = "$14.2M",
    val volumeChange: String = "Up by 8%",
    val range52w: String = "3.62 - 3.84",
    val previousClose: Double = 3.6691,
    val rsiValue: Double = 58.4,
    val rsiStatus: String = "Neutral",
    val instantConvertInput: Double = 100.0,
    val instantConvertResult: Double = 368.45
)

@HiltViewModel
class PairDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {

    private val base = savedStateHandle.get<String>("base") ?: "USD"
    private val target = savedStateHandle.get<String>("target") ?: "ILS"

    private val _uiState = MutableStateFlow(
        PairDetailUiState(
            baseCurrency = base,
            targetCurrency = target
        )
    )
    val uiState: StateFlow<PairDetailUiState> = _uiState.asStateFlow()

    init {
        loadPairData()
    }

    private fun loadPairData() {
        viewModelScope.launch {
            getExchangeRatesUseCase(base).collect { rates ->
                val match = rates.find { it.code.equals(target, ignoreCase = true) }
                if (match != null) {
                    val rate = match.rate
                    val low = rate * 0.995
                    val high = rate * 1.008
                    val absChange = rate * (match.change24h / 100.0)
                    _uiState.update { current ->
                        current.copy(
                            spotRate = rate,
                            changePercent24h = match.change24h,
                            changeAbsolute24h = absChange,
                            low24h = low,
                            high24h = high,
                            chartDataPoints = if (match.sparklineData.isNotEmpty()) match.sparklineData else current.chartDataPoints,
                            instantConvertResult = current.instantConvertInput * rate
                        )
                    }
                }
            }
        }
    }

    fun selectTimeframe(tf: String) {
        val simulatedData = when (tf) {
            "1D" -> listOf(3.6800f, 3.6815f, 3.6830f, 3.6820f, 3.6845f)
            "1W" -> listOf(3.6680f, 3.6710f, 3.6695f, 3.6740f, 3.6780f, 3.6810f, 3.6845f)
            "1M" -> listOf(3.6500f, 3.6620f, 3.6710f, 3.6650f, 3.6800f, 3.6845f)
            "1Y" -> listOf(3.6100f, 3.6400f, 3.7100f, 3.6800f, 3.7400f, 3.6845f)
            else -> listOf(3.5500f, 3.6000f, 3.7000f, 3.6500f, 3.8000f, 3.6845f)
        }
        _uiState.update { it.copy(selectedTimeframe = tf, chartDataPoints = simulatedData) }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun toggleAlert() {
        _uiState.update { it.copy(isAlertSet = !it.isAlertSet) }
    }
}
