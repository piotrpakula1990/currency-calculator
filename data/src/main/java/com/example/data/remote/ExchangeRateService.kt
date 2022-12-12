package com.example.data.remote

import com.example.data.remote.models.ExchangeRatesRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {

    @GET("latest")
    suspend fun latest(@Query("base") baseCurrency: String): ExchangeRatesRemote
}