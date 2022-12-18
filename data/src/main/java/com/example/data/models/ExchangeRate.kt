package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    val currency: Currency,
    val currencyRate: Float
)