package com.sarraf.app.data.repository

import com.sarraf.app.data.local.dao.ExchangeRateDao
import com.sarraf.app.data.local.entity.ExchangeRateEntity
import com.sarraf.app.data.local.entity.RateAlertEntity
import com.sarraf.app.data.preferences.UserPreferencesManager
import com.sarraf.app.data.remote.ExchangeRateApiService
import com.sarraf.app.domain.model.AlertConditionType
import com.sarraf.app.domain.model.ConversionResult
import com.sarraf.app.domain.model.CurrencyCategory
import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.domain.model.DeliveryChannel
import com.sarraf.app.domain.model.RateAlert
import com.sarraf.app.domain.model.TriggerFrequency
import com.sarraf.app.domain.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: ExchangeRateApiService,
    private val dao: ExchangeRateDao,
    private val preferencesManager: UserPreferencesManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyRepository {

    override fun getRatesFlow(baseCurrency: String): Flow<List<CurrencyRate>> {
        return dao.getRatesByBaseFlow(baseCurrency).map { entities ->
            if (entities.isEmpty()) {
                // Generate initial fallback / seed rates if room cache is empty
                getSeedRatesForBase(baseCurrency)
            } else {
                entities.map { it.toDomainModel() }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun refreshRates(baseCurrency: String): Result<Unit> = withContext(ioDispatcher) {
        try {
            val customApiKey = preferencesManager.apiKeyFlow.first()
            var ratesMap: Map<String, Double>? = null

            // If custom API key is supplied, try FreeCurrencyApi fallback first
            if (customApiKey.isNotBlank()) {
                try {
                    val fallbackResponse = apiService.getFreeCurrencyApiRates(
                        url = "https://api.freecurrencyapi.com/v1/latest",
                        apiKey = customApiKey,
                        baseCurrency = baseCurrency
                    )
                    if (fallbackResponse.isSuccessful && fallbackResponse.body() != null) {
                        ratesMap = fallbackResponse.body()!!.data
                    }
                } catch (_: Exception) {
                    // Fall back to open.er-api
                }
            }

            // Primary free live feed (open.er-api.com)
            if (ratesMap == null) {
                val response = apiService.getLatestRates(baseCurrency)
                if (response.isSuccessful && response.body() != null) {
                    ratesMap = response.body()!!.rates
                }
            }

            if (ratesMap != null && ratesMap.isNotEmpty()) {
                val now = System.currentTimeMillis()
                val entitiesToInsert = buildEntitiesFromRates(baseCurrency, ratesMap, now)
                dao.insertRates(entitiesToInsert)
                Result.success(Unit)
            } else {
                // If API failed, ensure seed rates are persisted so user is never blocked
                val cached = dao.getRatesByBaseFlow(baseCurrency).first()
                if (cached.isEmpty()) {
                    val seedEntities = getSeedRatesForBase(baseCurrency).map { it.toEntity(baseCurrency) }
                    dao.insertRates(seedEntities)
                }
                Result.failure(Exception("Could not retrieve latest exchange rates from remote provider."))
            }
        } catch (e: Exception) {
            // Offline recovery: ensure local data exists
            val cached = dao.getRatesByBaseFlow(baseCurrency).first()
            if (cached.isEmpty()) {
                val seedEntities = getSeedRatesForBase(baseCurrency).map { it.toEntity(baseCurrency) }
                dao.insertRates(seedEntities)
            }
            Result.failure(e)
        }
    }

    override suspend fun convertCurrency(
        fromCode: String,
        toCode: String,
        amount: Double
    ): Result<ConversionResult> = withContext(ioDispatcher) {
        try {
            if (fromCode == toCode) {
                return@withContext Result.success(
                    ConversionResult(
                        fromCode = fromCode,
                        toCode = toCode,
                        fromAmount = amount,
                        toAmount = amount,
                        exchangeRate = 1.0,
                        inverseRate = 1.0
                    )
                )
            }

            // Fetch base rates for fromCode
            var rates = dao.getRatesByBaseFlow(fromCode).first()
            if (rates.isEmpty()) {
                refreshRates(fromCode)
                rates = dao.getRatesByBaseFlow(fromCode).first()
            }

            val targetEntity = rates.find { it.currencyCode.equals(toCode, ignoreCase = true) }
            val rate = targetEntity?.rate ?: getFallbackRate(fromCode, toCode)
            val inverse = if (rate > 0.0) 1.0 / rate else 0.0
            val convertedAmount = amount * rate

            Result.success(
                ConversionResult(
                    fromCode = fromCode,
                    toCode = toCode,
                    fromAmount = amount,
                    toAmount = convertedAmount,
                    exchangeRate = rate,
                    inverseRate = inverse,
                    change24h = targetEntity?.change24h ?: 0.14
                )
            )
        } catch (e: Exception) {
            val fallbackRate = getFallbackRate(fromCode, toCode)
            val inverse = if (fallbackRate > 0.0) 1.0 / fallbackRate else 0.0
            Result.success(
                ConversionResult(
                    fromCode = fromCode,
                    toCode = toCode,
                    fromAmount = amount,
                    toAmount = amount * fallbackRate,
                    exchangeRate = fallbackRate,
                    inverseRate = inverse
                )
            )
        }
    }

    override fun getAlertsFlow(): Flow<List<RateAlert>> {
        return dao.getAllAlertsFlow().map { entities ->
            if (entities.isEmpty()) {
                getSeedAlerts()
            } else {
                entities.map { it.toDomainModel() }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun toggleAlert(alertId: String, isEnabled: Boolean) = withContext(ioDispatcher) {
        dao.updateAlertStatus(alertId, isEnabled)
    }

    override suspend fun saveAlert(alert: RateAlert) = withContext(ioDispatcher) {
        dao.insertAlert(alert.toEntity())
    }

    override suspend fun deleteAlert(alertId: String) = withContext(ioDispatcher) {
        dao.deleteAlert(alertId)
    }

    override suspend fun clearCache() = withContext(ioDispatcher) {
        dao.clearAllRates()
    }

    override fun getCachedCountFlow(): Flow<Int> {
        return dao.getTotalCachedCountFlow().flowOn(ioDispatcher)
    }

    private fun buildEntitiesFromRates(
        baseCurrency: String,
        ratesMap: Map<String, Double>,
        timestamp: Long
    ): List<ExchangeRateEntity> {
        val metaData = getCurrencyMetadata()
        val list = mutableListOf<ExchangeRateEntity>()

        for ((code, meta) in metaData) {
            val rateValue = ratesMap[code] ?: continue
            val sparkline = generateSparklineValues(rateValue, meta.change24h)
            list.add(
                ExchangeRateEntity(
                    id = "${baseCurrency}_$code",
                    baseCurrency = baseCurrency,
                    currencyCode = code,
                    currencyName = meta.name,
                    currencySymbol = meta.symbol,
                    flag = meta.flag,
                    rate = rateValue,
                    change24h = meta.change24h,
                    sparklineJson = sparkline.joinToString(","),
                    category = meta.category.name,
                    subtitle = meta.subtitle,
                    timestamp = timestamp
                )
            )
        }
        return list
    }

    private fun generateSparklineValues(currentRate: Double, changePercent: Double): List<Float> {
        val count = 8
        val baseMultiplier = 1.0 - (changePercent / 100.0)
        val start = (currentRate * baseMultiplier).toFloat()
        val end = currentRate.toFloat()
        val result = mutableListOf<Float>()
        val step = (end - start) / (count - 1)

        val variations = listOf(0.0f, 0.003f, -0.002f, 0.004f, 0.001f, -0.001f, 0.002f, 0.0f)
        for (i in 0 until count) {
            val valAtI = start + (step * i) + (start * variations[i % variations.size])
            result.add(valAtI)
        }
        return result
    }

    private fun getFallbackRate(from: String, to: String): Double {
        if (from == to) return 1.0
        val usdRates = mapOf(
            "USD" to 1.0,
            "ILS" to 3.6845,
            "EUR" to 0.9234,
            "JOD" to 0.7090,
            "SAR" to 3.7505,
            "AED" to 3.6725,
            "EGP" to 48.9500,
            "KWD" to 0.3072,
            "QAR" to 3.6450,
            "GBP" to 0.7892,
            "TRY" to 34.2250
        )
        val fromRate = usdRates[from] ?: 1.0
        val toRate = usdRates[to] ?: 1.0
        return toRate / fromRate
    }

    private fun getSeedRatesForBase(base: String): List<CurrencyRate> {
        val meta = getCurrencyMetadata()
        val list = mutableListOf<CurrencyRate>()
        for ((code, info) in meta) {
            val rateVal = getFallbackRate(base, code)
            val sparkline = generateSparklineValues(rateVal, info.change24h)
            list.add(
                CurrencyRate(
                    code = code,
                    name = info.name,
                    symbol = info.symbol,
                    flag = info.flag,
                    rate = rateVal,
                    change24h = info.change24h,
                    sparklineData = sparkline,
                    category = info.category,
                    subtitle = info.subtitle
                )
            )
        }
        return list
    }

    private fun getSeedAlerts(): List<RateAlert> {
        return listOf(
            RateAlert(
                id = "alert-1",
                baseCode = "ILS",
                targetCode = "USD",
                targetRate = 3.7000,
                currentRate = 3.6845,
                conditionType = AlertConditionType.RISES_ABOVE,
                isEnabled = true,
                triggerFrequency = TriggerFrequency.ONCE,
                deliveryChannels = listOf(DeliveryChannel.PUSH)
            ),
            RateAlert(
                id = "alert-2",
                baseCode = "USD",
                targetCode = "EUR",
                targetRate = 1.0800,
                currentRate = 1.0832,
                conditionType = AlertConditionType.FALLS_BELOW,
                isEnabled = false,
                triggerFrequency = TriggerFrequency.RECURRING,
                deliveryChannels = listOf(DeliveryChannel.PUSH, DeliveryChannel.EMAIL)
            ),
            RateAlert(
                id = "alert-3",
                baseCode = "ILS",
                targetCode = "JOD",
                targetRate = 5.2500,
                currentRate = 5.1960,
                conditionType = AlertConditionType.VOLATILITY_SWING,
                isEnabled = true,
                triggerFrequency = TriggerFrequency.RECURRING,
                deliveryChannels = listOf(DeliveryChannel.PUSH, DeliveryChannel.SMS)
            )
        )
    }

    private fun getCurrencyMetadata(): Map<String, CurrencyMeta> = mapOf(
        "ILS" to CurrencyMeta("Israeli Shekel", "₪", "🇵🇸", 0.42, CurrencyCategory.PRIMARY, "Active Volume"),
        "EUR" to CurrencyMeta("Euro", "€", "🇪🇺", -0.18, CurrencyCategory.PRIMARY, "Frankfurt FX session"),
        "SAR" to CurrencyMeta("Saudi Riyal", "SAR", "🇸🇦", 0.01, CurrencyCategory.PRIMARY, "USD Pegged"),
        "JOD" to CurrencyMeta("Jordanian Dinar", "JOD", "🇯🇴", 0.02, CurrencyCategory.PRIMARY, "Fixed Pegged"),
        "AED" to CurrencyMeta("UAE Dirham", "AED", "🇦🇪", 0.00, CurrencyCategory.REGIONAL, "Pegged rate"),
        "EGP" to CurrencyMeta("Egyptian Pound", "EGP", "🇪🇬", -0.65, CurrencyCategory.REGIONAL, "Parallel market spread"),
        "KWD" to CurrencyMeta("Kuwaiti Dinar", "KWD", "🇰🇼", 0.05, CurrencyCategory.REGIONAL, "Highest global valuation"),
        "QAR" to CurrencyMeta("Qatari Riyal", "QAR", "🇶🇦", 0.03, CurrencyCategory.REGIONAL, "USD Pegged"),
        "GBP" to CurrencyMeta("British Pound", "£", "🇬🇧", 0.31, CurrencyCategory.GLOBAL, "London FX session"),
        "TRY" to CurrencyMeta("Turkish Lira", "₺", "🇹🇷", -1.15, CurrencyCategory.GLOBAL, "High volatility")
    )

    private data class CurrencyMeta(
        val name: String,
        val symbol: String,
        val flag: String,
        val change24h: Double,
        val category: CurrencyCategory,
        val subtitle: String
    )
}

// Mapper extension functions
fun ExchangeRateEntity.toDomainModel(): CurrencyRate {
    val sparklineList = sparklineJson.split(",")
        .mapNotNull { it.trim().toFloatOrNull() }

    val cat = try {
        CurrencyCategory.valueOf(category)
    } catch (_: Exception) {
        CurrencyCategory.GLOBAL
    }

    return CurrencyRate(
        code = currencyCode,
        name = currencyName,
        symbol = currencySymbol,
        flag = flag,
        rate = rate,
        change24h = change24h,
        sparklineData = sparklineList,
        category = cat,
        subtitle = subtitle,
        lastUpdated = timestamp
    )
}

fun CurrencyRate.toEntity(baseCurrency: String): ExchangeRateEntity {
    return ExchangeRateEntity(
        id = "${baseCurrency}_$code",
        baseCurrency = baseCurrency,
        currencyCode = code,
        currencyName = name,
        currencySymbol = symbol,
        flag = flag,
        rate = rate,
        change24h = change24h,
        sparklineJson = sparklineData.joinToString(","),
        category = category.name,
        subtitle = subtitle,
        timestamp = lastUpdated
    )
}

fun RateAlertEntity.toDomainModel(): RateAlert {
    val cond = try {
        AlertConditionType.valueOf(conditionType)
    } catch (_: Exception) {
        AlertConditionType.RISES_ABOVE
    }

    val freq = try {
        TriggerFrequency.valueOf(triggerFrequency)
    } catch (_: Exception) {
        TriggerFrequency.ONCE
    }

    val channels = deliveryChannels.split(",").mapNotNull { name ->
        try {
            DeliveryChannel.valueOf(name.trim())
        } catch (_: Exception) {
            null
        }
    }

    return RateAlert(
        id = id,
        baseCode = baseCode,
        targetCode = targetCode,
        targetRate = targetRate,
        currentRate = currentRate,
        conditionType = cond,
        isEnabled = isEnabled,
        triggerFrequency = freq,
        deliveryChannels = if (channels.isEmpty()) listOf(DeliveryChannel.PUSH) else channels,
        lastTriggered = lastTriggered
    )
}

fun RateAlert.toEntity(): RateAlertEntity {
    return RateAlertEntity(
        id = id,
        baseCode = baseCode,
        targetCode = targetCode,
        targetRate = targetRate,
        currentRate = currentRate,
        conditionType = conditionType.name,
        isEnabled = isEnabled,
        triggerFrequency = triggerFrequency.name,
        deliveryChannels = deliveryChannels.joinToString(",") { it.name },
        lastTriggered = lastTriggered
    )
}
