package com.example.data.local

import com.example.data.models.Currency
import com.example.data.models.ExchangeRates
import com.example.data.models.Settings
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun setExchangeRates(exchangeRates: ExchangeRates)

    fun getExchangeRates(): Flow<ExchangeRates>

    suspend fun setBaseCurrency(currency: Currency)

    suspend fun setValuePrecision(precision: Int)

    suspend fun setCurrenciesOrder(order: List<Currency>)

    fun getSettings(): Flow<Settings>
}