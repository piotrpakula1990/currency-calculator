package com.example.data.respositories

import com.example.data.api.models.ExchangeCurrencyRates

interface CurrencyRepository {

    suspend fun getExchangeCurrencyRates(baseCurrency: String) : ExchangeCurrencyRates
}