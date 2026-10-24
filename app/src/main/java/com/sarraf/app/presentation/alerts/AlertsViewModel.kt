package com.sarraf.app.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarraf.app.domain.model.AlertConditionType
import com.sarraf.app.domain.model.DeliveryChannel
import com.sarraf.app.domain.model.RateAlert
import com.sarraf.app.domain.model.TriggerFrequency
import com.sarraf.app.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AlertsUiState(
    val activeAlerts: List<RateAlert> = emptyList(),
    val triggeredHistory: List<TriggeredAlertHistoryItem> = emptyList(),
    val selectedChannels: Set<DeliveryChannel> = setOf(DeliveryChannel.PUSH),
    val showCreateDialog: Boolean = false,
    val dialogBase: String = "USD",
    val dialogTarget: String = "ILS",
    val dialogTargetPrice: String = "3.7200",
    val dialogCondition: AlertConditionType = AlertConditionType.RISES_ABOVE
)

data class TriggeredAlertHistoryItem(
    val id: String,
    val title: String,
    val pairBadge: String,
    val message: String,
    val timeLabel: String,
    val dateLabel: String,
    val isPositive: Boolean
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AlertsUiState(
            triggeredHistory = listOf(
                TriggeredAlertHistoryItem(
                    id = "hist-1",
                    title = "Target Price Hit",
                    pairBadge = "USD / ILS",
                    message = "Crossed target 3.6800 ₪ and reached 3.6845 ₪",
                    timeLabel = "10:15 AM",
                    dateLabel = "Today",
                    isPositive = true
                ),
                TriggeredAlertHistoryItem(
                    id = "hist-2",
                    title = "Dip Alert Triggered",
                    pairBadge = "EUR / USD",
                    message = "Fell below $1.0850 to $1.0832",
                    timeLabel = "04:20 PM",
                    dateLabel = "Yesterday",
                    isPositive = false
                )
            )
        )
    )
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAlertsFlow().collect { alerts ->
                _uiState.update { it.copy(activeAlerts = alerts) }
            }
        }
    }

    fun toggleAlert(alertId: String, currentEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleAlert(alertId, !currentEnabled)
            _uiState.update { state ->
                state.copy(
                    activeAlerts = state.activeAlerts.map {
                        if (it.id == alertId) it.copy(isEnabled = !currentEnabled) else it
                    }
                )
            }
        }
    }

    fun toggleDeliveryChannel(channel: DeliveryChannel) {
        _uiState.update { current ->
            val set = current.selectedChannels.toMutableSet()
            if (set.contains(channel)) {
                if (set.size > 1) set.remove(channel)
            } else {
                set.add(channel)
            }
            current.copy(selectedChannels = set)
        }
    }

    fun clearHistory() {
        _uiState.update { it.copy(triggeredHistory = emptyList()) }
    }

    fun showCreateAlert(show: Boolean) {
        _uiState.update { it.copy(showCreateDialog = show) }
    }

    fun updateDialogBase(base: String) {
        _uiState.update { it.copy(dialogBase = base) }
    }

    fun updateDialogTarget(target: String) {
        _uiState.update { it.copy(dialogTarget = target) }
    }

    fun updateDialogTargetPrice(price: String) {
        _uiState.update { it.copy(dialogTargetPrice = price) }
    }

    fun updateDialogCondition(condition: AlertConditionType) {
        _uiState.update { it.copy(dialogCondition = condition) }
    }

    fun saveNewAlert() {
        val state = _uiState.value
        val rateNum = state.dialogTargetPrice.toDoubleOrNull() ?: 3.7000
        val newAlert = RateAlert(
            id = "alert-${System.currentTimeMillis()}",
            baseCode = state.dialogBase,
            targetCode = state.dialogTarget,
            targetRate = rateNum,
            currentRate = 3.6845,
            conditionType = state.dialogCondition,
            isEnabled = true,
            triggerFrequency = TriggerFrequency.ONCE,
            deliveryChannels = state.selectedChannels.toList()
        )
        viewModelScope.launch {
            repository.saveAlert(newAlert)
            _uiState.update {
                it.copy(
                    activeAlerts = listOf(newAlert) + it.activeAlerts,
                    showCreateDialog = false,
                    dialogTargetPrice = "3.7200"
                )
            }
        }
    }
}
