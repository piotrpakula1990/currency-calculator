package com.example.data.api

import com.example.data.api.models.ExchangeCurrencyRates
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {

    @GET("latest")
    suspend fun latest(@Query("base") baseCurrency: String): ExchangeCurrencyRates
}