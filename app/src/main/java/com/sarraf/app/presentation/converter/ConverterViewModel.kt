package com.sarraf.app.presentation.converter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarraf.app.data.preferences.UserPreferencesManager
import com.sarraf.app.domain.model.ConversionResult
import com.sarraf.app.domain.usecase.ConvertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class ConverterUiState(
    val fromCurrency: String = "USD",
    val toCurrency: String = "ILS",
    val inputAmountStr: String = "1000",
    val calculatedResult: Double = 3684.50,
    val exchangeRate: Double = 3.6845,
    val inverseRate: Double = 0.2714,
    val rateChangeToday: Double = 0.14,
    val isSwapping: Boolean = false,
    val swapRotation: Float = 0f,
    val isCopied: Boolean = false,
    val isHapticEnabled: Boolean = true,
    val showCurrencyPicker: Boolean = false,
    val pickerTargetField: String = "FROM", // "FROM" or "TO"
    val recentConversions: List<RecentConversionItem> = emptyList()
)

data class RecentConversionItem(
    val id: String,
    val fromCode: String,
    val toCode: String,
    val fromAmount: Double,
    val toAmount: Double,
    val flag: String,
    val subtitle: String,
    val timeLabel: String,
    val status: String = "Success"
)

data class CurrencyPickerOption(
    val code: String,
    val name: String,
    val flag: String,
    val symbol: String
)

@HiltViewModel
class ConverterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val initialBase = savedStateHandle.get<String>("base") ?: "USD"
    private val initialTarget = savedStateHandle.get<String>("target") ?: "ILS"

    val availableCurrencies = listOf(
        CurrencyPickerOption("USD", "United States Dollar", "🇺🇸", "$"),
        CurrencyPickerOption("EUR", "Euro", "🇪🇺", "€"),
        CurrencyPickerOption("ILS", "Israeli Shekel", "🇵🇸", "₪"),
        CurrencyPickerOption("JOD", "Jordanian Dinar", "🇯🇴", "د.أ"),
        CurrencyPickerOption("SAR", "Saudi Riyal", "🇸🇦", "SAR"),
        CurrencyPickerOption("AED", "UAE Dirham", "🇦🇪", "AED"),
        CurrencyPickerOption("EGP", "Egyptian Pound", "🇪🇬", "EGP"),
        CurrencyPickerOption("KWD", "Kuwaiti Dinar", "🇰🇼", "KWD"),
        CurrencyPickerOption("QAR", "Qatari Riyal", "🇶🇦", "QAR"),
        CurrencyPickerOption("GBP", "British Pound", "🇬🇧", "£"),
        CurrencyPickerOption("TRY", "Turkish Lira", "🇹🇷", "₺")
    )

    private val _uiState = MutableStateFlow(
        ConverterUiState(
            fromCurrency = initialBase,
            toCurrency = initialTarget,
            recentConversions = listOf(
                RecentConversionItem(
                    id = "rec-1",
                    fromCode = "USD",
                    toCode = "JOD",
                    fromAmount = 500.0,
                    toAmount = 354.50,
                    flag = "🇯🇴",
                    subtitle = "US Dollar to Jordanian Dinar",
                    timeLabel = "Today 10:15 AM"
                ),
                RecentConversionItem(
                    id = "rec-2",
                    fromCode = "EUR",
                    toCode = "ILS",
                    fromAmount = 1200.0,
                    toAmount = 4780.20,
                    flag = "🇪🇺",
                    subtitle = "Euro to Israeli Shekel",
                    timeLabel = "Yesterday"
                )
            )
        )
    )
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.hapticsEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(isHapticEnabled = enabled) }
            }
        }
        performRecalculation()
    }

    fun onDigitPressed(digit: String) {
        val current = _uiState.value.inputAmountStr
        val updated = when {
            digit == "." -> {
                if (current.contains(".")) current else "$current."
            }
            current == "0" -> digit
            current.length < 9 -> current + digit
            else -> current
        }
        _uiState.update { it.copy(inputAmountStr = updated) }
        performRecalculation()
    }

    fun onBackspacePressed() {
        val current = _uiState.value.inputAmountStr
        val updated = if (current.length > 1) {
            current.dropLast(1)
        } else {
            "0"
        }
        _uiState.update { it.copy(inputAmountStr = updated) }
        performRecalculation()
    }

    fun onQuickAdd(amount: Double) {
        val currentAmount = _uiState.value.inputAmountStr.toDoubleOrNull() ?: 0.0
        val sum = currentAmount + amount
        val formatted = if (sum % 1.0 == 0.0) sum.toLong().toString() else String.format(Locale.US, "%.2f", sum)
        _uiState.update { it.copy(inputAmountStr = formatted) }
        performRecalculation()
    }

    fun swapCurrencies() {
        val from = _uiState.value.fromCurrency
        val to = _uiState.value.toCurrency
        _uiState.update {
            it.copy(
                fromCurrency = to,
                toCurrency = from,
                swapRotation = it.swapRotation + 180f
            )
        }
        performRecalculation()
    }

    fun openCurrencyPicker(targetField: String) {
        _uiState.update { it.copy(showCurrencyPicker = true, pickerTargetField = targetField) }
    }

    fun closeCurrencyPicker() {
        _uiState.update { it.copy(showCurrencyPicker = false) }
    }

    fun selectCurrencyFromPicker(currencyCode: String) {
        val field = _uiState.value.pickerTargetField
        _uiState.update { current ->
            if (field == "FROM") {
                current.copy(fromCurrency = currencyCode, showCurrencyPicker = false)
            } else {
                current.copy(toCurrency = currencyCode, showCurrencyPicker = false)
            }
        }
        performRecalculation()
    }

    fun setCurrencies(from: String, to: String) {
        _uiState.update { it.copy(fromCurrency = from, toCurrency = to) }
        performRecalculation()
    }

    fun onCopyResult() {
        _uiState.update { it.copy(isCopied = true) }
    }

    fun resetCopyState() {
        _uiState.update { it.copy(isCopied = false) }
    }

    private fun performRecalculation() {
        viewModelScope.launch {
            val from = _uiState.value.fromCurrency
            val to = _uiState.value.toCurrency
            val amount = _uiState.value.inputAmountStr.toDoubleOrNull() ?: 0.0

            val result = convertCurrencyUseCase(from, to, amount)
            result.onSuccess { conv ->
                _uiState.update {
                    it.copy(
                        calculatedResult = conv.toAmount,
                        exchangeRate = conv.exchangeRate,
                        inverseRate = conv.inverseRate,
                        rateChangeToday = conv.change24h
                    )
                }
            }
        }
    }
}
