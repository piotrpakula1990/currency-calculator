package com.example.data.models

data class Settings(
    val baseCurrency: Currency,
    val valuePrecision: Int,
    val order: List<Currency>
)