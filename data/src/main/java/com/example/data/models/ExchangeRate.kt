package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    val currencyShortcut: String,
    val currencyRate: Float
)