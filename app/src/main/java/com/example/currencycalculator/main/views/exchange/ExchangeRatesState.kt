package com.example.currencycalculator.main.views.exchange

data class ExchangeRatesUiState(
    val baseCurrency: String,
    val baseCurrencyValue: Float,
    val outputs: List<CalculatedExchangeRate> = listOf(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

data class CalculatedExchangeRate(
    val currency: String,
    val rate: Float,
    val calculatedValue: Float
)

sealed class ExchangeRatesAction {
    class SetCurrency(val currency: String) : ExchangeRatesAction()
    class SetValue(val value: Float) : ExchangeRatesAction()
}