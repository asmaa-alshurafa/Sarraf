package com.sarraf.app.domain.model

data class CurrencyRate(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: String,
    val rate: Double,
    val change24h: Double,
    val sparklineData: List<Float> = emptyList(),
    val category: CurrencyCategory = CurrencyCategory.GLOBAL,
    val subtitle: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class CurrencyCategory {
    PRIMARY,
    REGIONAL,
    GLOBAL
}

data class ConversionResult(
    val fromCode: String,
    val toCode: String,
    val fromAmount: Double,
    val toAmount: Double,
    val exchangeRate: Double,
    val inverseRate: Double,
    val change24h: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

data class RateAlert(
    val id: String,
    val baseCode: String,
    val targetCode: String,
    val targetRate: Double,
    val currentRate: Double,
    val conditionType: AlertConditionType,
    val isEnabled: Boolean = true,
    val triggerFrequency: TriggerFrequency = TriggerFrequency.ONCE,
    val deliveryChannels: List<DeliveryChannel> = listOf(DeliveryChannel.PUSH),
    val lastTriggered: Long? = null
)

enum class AlertConditionType {
    RISES_ABOVE,
    FALLS_BELOW,
    VOLATILITY_SWING
}

enum class TriggerFrequency {
    ONCE,
    RECURRING
}

enum class DeliveryChannel {
    PUSH,
    SMS,
    EMAIL
}
