package com.example.currencycalculator.main.views.exchange

import com.example.data.models.Currency
import com.example.data.models.ExchangeRate

sealed class ExchangeRatesUiState(
    open val value: Float = 1f,
    open val baseCurrency: Currency = Currency.UNK
) {
    object Initialize : ExchangeRatesUiState()

    data class Loading(
        override val value: Float,
        override val baseCurrency: Currency
    ) : ExchangeRatesUiState()

    data class Error(
        override val value: Float,
        override val baseCurrency: Currency,
        val error: Throwable
    ) : ExchangeRatesUiState()

    data class Result(
        override val value: Float,
        override val baseCurrency: Currency,
        val outputs: List<CalculatedExchangeRate>
    ) : ExchangeRatesUiState()
}

data class CalculatedExchangeRate(
    val exchangeRate: ExchangeRate,
    val calculatedValue: Float
)

sealed class ExchangeRatesAction {
    class SetCurrency(val currency: Currency) : ExchangeRatesAction()
    class SetValue(val value: Float) : ExchangeRatesAction()
}