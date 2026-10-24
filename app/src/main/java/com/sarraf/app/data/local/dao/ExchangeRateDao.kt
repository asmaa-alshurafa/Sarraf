package com.sarraf.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarraf.app.data.local.entity.ExchangeRateEntity
import com.sarraf.app.data.local.entity.RateAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates WHERE baseCurrency = :baseCurrency ORDER BY CASE category WHEN 'PRIMARY' THEN 1 WHEN 'REGIONAL' THEN 2 ELSE 3 END, currencyCode ASC")
    fun getRatesByBaseFlow(baseCurrency: String): Flow<List<ExchangeRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)

    @Query("DELETE FROM exchange_rates WHERE baseCurrency = :baseCurrency")
    suspend fun deleteRatesForBase(baseCurrency: String)

    @Query("DELETE FROM exchange_rates")
    suspend fun clearAllRates()

    @Query("SELECT COUNT(*) FROM exchange_rates")
    fun getTotalCachedCountFlow(): Flow<Int>

    // Rate Alerts
    @Query("SELECT * FROM rate_alerts ORDER BY id ASC")
    fun getAllAlertsFlow(): Flow<List<RateAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: RateAlertEntity)

    @Query("UPDATE rate_alerts SET isEnabled = :isEnabled WHERE id = :alertId")
    suspend fun updateAlertStatus(alertId: String, isEnabled: Boolean)

    @Query("DELETE FROM rate_alerts WHERE id = :alertId")
    suspend fun deleteAlert(alertId: String)

    @Query("DELETE FROM rate_alerts")
    suspend fun clearAllAlerts()
}
