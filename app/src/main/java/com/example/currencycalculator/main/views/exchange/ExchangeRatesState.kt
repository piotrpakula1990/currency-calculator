package com.example.currencycalculator.main.views.exchange

import com.example.data.models.Currency

data class ExchangeRatesUiState(
    val baseCurrency: Currency,
    val baseCurrencyValue: Float,
    val outputs: List<CalculatedExchangeRate> = listOf(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

data class CalculatedExchangeRate(
    val currency: Currency,
    val rate: Float,
    val calculatedValue: Float
)

sealed class ExchangeRatesAction {
    class SetCurrency(val currency: Currency) : ExchangeRatesAction()
    class SetValue(val value: Float) : ExchangeRatesAction()
}