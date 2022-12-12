package com.example.data.local

import com.example.data.models.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun setExchangeRates(exchangeRates: ExchangeRates)

    fun getExchangeRates(): Flow<ExchangeRates>
}