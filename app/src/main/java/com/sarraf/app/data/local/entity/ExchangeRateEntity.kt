package com.sarraf.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey
    val id: String, // Composite key e.g. "USD_EUR"
    val baseCurrency: String,
    val currencyCode: String,
    val currencyName: String,
    val currencySymbol: String,
    val flag: String,
    val rate: Double,
    val change24h: Double,
    val sparklineJson: String, // Comma-separated floats e.g. "0.92,0.91,0.9234"
    val category: String,
    val subtitle: String,
    val timestamp: Long
)

@Entity(tableName = "rate_alerts")
data class RateAlertEntity(
    @PrimaryKey
    val id: String,
    val baseCode: String,
    val targetCode: String,
    val targetRate: Double,
    val currentRate: Double,
    val conditionType: String,
    val isEnabled: Boolean,
    val triggerFrequency: String,
    val deliveryChannels: String,
    val lastTriggered: Long?
)
