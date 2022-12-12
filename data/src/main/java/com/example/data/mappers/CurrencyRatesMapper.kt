package com.example.data.mappers

import com.example.data.models.ExchangeRate
import com.example.data.models.ExchangeRates
import com.example.data.remote.models.ExchangeRatesRemote
import org.joda.time.DateTime

fun ExchangeRatesRemote.asExchangeRates() = ExchangeRates(
    baseCurrency = base,
    date = DateTime.parse(date),
    rates = rates.map { ExchangeRate(it.key, it.value) }
)