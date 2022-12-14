package com.example.data.models

import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class ExchangeRates(

    val baseCurrency: String,

    @Serializable(DateTimeSerializer::class)
    val date: DateTime,

    val rates: List<ExchangeRate>
)