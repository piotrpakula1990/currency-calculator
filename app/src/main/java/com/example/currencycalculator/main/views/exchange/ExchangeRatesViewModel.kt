package com.example.currencycalculator.main.views.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencycalculator.utils.Currency
import com.example.data.models.ExchangeRates
import com.example.data.respositories.CurrencyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExchangeRatesViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {

    private val statePrivate = MutableStateFlow(getDefaultState())
    val state: StateFlow<ExchangeRatesUiState> get() = statePrivate

    var loadingJob: Job? = null

    init {
        refreshData(
            currency = state.value.baseCurrency,
            value = state.value.baseCurrencyValue
        )
    }

    fun intent(action: ExchangeRatesAction) {
        when (action) {
            is ExchangeRatesAction.SetCurrency -> refreshData(
                currency = action.currency,
                value = state.value.baseCurrencyValue
            )
            is ExchangeRatesAction.SetValue -> refreshData(
                currency = state.value.baseCurrency,
                value = action.value
            )
        }
    }

    private fun getDefaultState(): ExchangeRatesUiState {
        return ExchangeRatesUiState(
            baseCurrency = DEFAULT_CURRENCY,
            baseCurrencyValue = DEFAULT_CURRENCY_VALUE,
            isEmpty = true
        )
    }

    private fun refreshData(currency: String, value: Float) {
        if (loadingJob?.isActive == true) loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            val newState = ExchangeRatesUiState(baseCurrency = currency, baseCurrencyValue = value)
            try {
                statePrivate.emit(newState.copy(isLoading = true))

                val exchangeRates = currencyRepository
                    .getExchangeRates(currency)
                    .map { filterKnownCurrencies(it) }
                    .single()
                delay(LOADING_DELAY)

                statePrivate.emit(newState.copy(outputs = calculateRates(value, exchangeRates)))
            } catch (e: Exception) {
                statePrivate.emit(newState.copy(error = e))
            }
        }
    }

    private fun calculateRates(
        value: Float,
        exchangeRates: ExchangeRates
    ): List<CalculatedExchangeRate> {
        return exchangeRates.rates.map { exchangeRate ->
            CalculatedExchangeRate(
                currency = exchangeRate.currencyShortcut,
                rate = exchangeRate.currencyRate,
                calculatedValue = exchangeRate.currencyRate * value
            )
        }
    }

    private fun filterKnownCurrencies(exchangeRates: ExchangeRates): ExchangeRates {
        return exchangeRates.copy(
            rates = exchangeRates.rates.filter { Currency.isExist(it.currencyShortcut) }
        )
    }

    companion object {
        const val DEFAULT_CURRENCY = "USD"
        const val DEFAULT_CURRENCY_VALUE = 1f
        const val LOADING_DELAY = 1000L
    }
}