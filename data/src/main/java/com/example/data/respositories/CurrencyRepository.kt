package com.example.data.respositories

import com.example.data.models.Currency
import com.example.data.models.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun getExchangeRates(baseCurrency: Currency): Flow<ExchangeRates>
}