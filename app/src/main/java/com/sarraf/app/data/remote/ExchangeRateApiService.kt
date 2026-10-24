package com.sarraf.app.data.remote

import com.sarraf.app.data.remote.dto.FreeCurrencyApiResponseDto
import com.sarraf.app.data.remote.dto.OpenErApiResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ExchangeRateApiService {

    /**
     * Primary live public endpoint from open.er-api.com
     * E.g. https://open.er-api.com/v6/latest/USD
     */
    @GET("v6/latest/{base}")
    suspend fun getLatestRates(
        @Path("base") base: String
    ): Response<OpenErApiResponseDto>

    /**
     * Fallback endpoint for FreeCurrencyAPI with user-provided API key
     * E.g. https://api.freecurrencyapi.com/v1/latest?apikey=...&base_currency=USD
     */
    @GET
    suspend fun getFreeCurrencyApiRates(
        @Url url: String,
        @Query("apikey") apiKey: String,
        @Query("base_currency") baseCurrency: String
    ): Response<FreeCurrencyApiResponseDto>
}
