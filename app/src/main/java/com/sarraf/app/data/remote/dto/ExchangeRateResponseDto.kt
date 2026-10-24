package com.sarraf.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenErApiResponseDto(
    @SerialName("result")
    val result: String? = null,
    @SerialName("provider")
    val provider: String? = null,
    @SerialName("base_code")
    val baseCode: String = "USD",
    @SerialName("time_last_update_unix")
    val timeLastUpdateUnix: Long = 0L,
    @SerialName("time_last_update_utc")
    val timeLastUpdateUtc: String = "",
    @SerialName("rates")
    val rates: Map<String, Double> = emptyMap()
)

@Serializable
data class FreeCurrencyApiResponseDto(
    @SerialName("data")
    val data: Map<String, Double> = emptyMap()
)
