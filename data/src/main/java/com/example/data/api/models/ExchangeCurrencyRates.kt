package com.example.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeCurrencyRates(

    val success: Boolean,

    val base: String,

    val date: String,

    val rates: Map<String, Float>
)