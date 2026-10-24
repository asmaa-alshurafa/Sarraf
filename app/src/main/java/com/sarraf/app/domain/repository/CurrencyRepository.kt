package com.sarraf.app.domain.repository

import com.sarraf.app.domain.model.ConversionResult
import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.domain.model.RateAlert
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getRatesFlow(baseCurrency: String): Flow<List<CurrencyRate>>
    suspend fun refreshRates(baseCurrency: String): Result<Unit>
    suspend fun convertCurrency(fromCode: String, toCode: String, amount: Double): Result<ConversionResult>
    fun getAlertsFlow(): Flow<List<RateAlert>>
    suspend fun toggleAlert(alertId: String, isEnabled: Boolean)
    suspend fun saveAlert(alert: RateAlert)
    suspend fun deleteAlert(alertId: String)
    suspend fun clearCache()
    fun getCachedCountFlow(): Flow<Int>
}
