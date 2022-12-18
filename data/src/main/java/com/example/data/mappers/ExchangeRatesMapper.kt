package com.example.data.mappers

import com.example.data.models.Currency
import com.example.data.models.ExchangeRate
import com.example.data.models.ExchangeRates
import com.example.data.models.exceptions.MappingException
import com.example.data.remote.models.ExchangeRatesRemote
import org.joda.time.DateTime

fun ExchangeRatesRemote.asExchangeRates() = ExchangeRates(
    baseCurrency = Currency.getOrNull(base)
        ?: throw MappingException("Cannot recognize base currency"),
    date = DateTime.parse(date),
    rates = rates.mapNotNull { rate ->
        val currency = Currency.getOrNull(rate.key) ?: return@mapNotNull null
        ExchangeRate(currency, rate.value)
    }
)