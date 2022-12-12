package com.example.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesRemote(

    val success: Boolean,

    val base: String,

    val date: String,

    val rates: Map<String, Float>
)