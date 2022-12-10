package com.example.data.respositories

import com.example.data.api.Retrofit
import com.example.data.api.models.ExchangeCurrencyRates

class CurrencyRepositoryImpl(private val retrofit: Retrofit) : CurrencyRepository {

    override suspend fun getExchangeCurrencyRates(baseCurrency: String): ExchangeCurrencyRates {
        return retrofit.exchangeRateService.latest(baseCurrency)
    }
}