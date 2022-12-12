package com.example.data.models

import org.joda.time.DateTime

data class ExchangeRates(
    val baseCurrency: String,
    val date: DateTime,
    val rates: List<ExchangeRate>
)