package com.example.currencycalculator.main.views.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencycalculator.main.views.exchange.ExchangeRatesAction.SetCurrency
import com.example.currencycalculator.main.views.exchange.ExchangeRatesAction.SetValue
import com.example.currencycalculator.main.views.exchange.ExchangeRatesUiState.*
import com.example.data.models.Currency
import com.example.data.models.ExchangeRate
import com.example.data.models.Settings
import com.example.data.respositories.currency.CurrencyRepository
import com.example.data.respositories.settings.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat

class ExchangeRatesViewModel(
    private val currencyRepository: CurrencyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ExchangeRatesUiState>(Initialize)
    val state: StateFlow<ExchangeRatesUiState> get() = _state

    private lateinit var settings: Settings
    private var loadingJob: Job? = null

    init {
        Timber.d("Initialize view model ($this)")
        viewModelScope.launch {
            settings = settingsRepository.getSettings().first()
            reload(settings.baseCurrency)
        }
    }

    fun intent(action: ExchangeRatesAction) {
        when (action) {
            is SetCurrency -> {
                Timber.i("Set currency: ${action.currency}")
                reload(action.currency)
            }
            is SetValue -> {
                Timber.i("Set value: ${action.value}")
                recalculate(action.value)
            }
        }
    }

    private fun reload(currency: Currency) {
        if (loadingJob?.isActive == true) loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            try {
                val loading = Loading(value = state.value.value, baseCurrency = currency)
                _state.emit(loading)

                delay(LOADING_DELAY)
                val exchangeRates = currencyRepository.getExchangeRates(currency).single()

                val result = Result(
                    value = state.value.value,
                    baseCurrency = currency,
                    outputs = calculateOutputs(exchangeRates.rates)
                )

                Timber.d("Reload succeed: $result")
                _state.emit(result)
            } catch (e: Exception) {
                Timber.e("Error at reload: $e")
                val error = Error(
                    value = state.value.value,
                    baseCurrency = currency,
                    error = e
                )
                _state.emit(error)
            }
        }
    }

    private fun recalculate(value: Float) {
        viewModelScope.launch {
            val previousState = state.value as? Result ?: return@launch
            val formattedValue = formatValue(value)
            val outputs = previousState.outputs
                .map { calculateRate(it.exchangeRate, formattedValue) }

            val result = previousState.copy(value = formattedValue, outputs = outputs)
            Timber.d("Recalculate succeed")
            _state.emit(result)
        }
    }

    private fun calculateOutputs(rates: List<ExchangeRate>): List<CalculatedExchangeRate> {
        return settings.order
            .mapNotNull { currency -> rates.firstOrNull { it.currency == currency } }
            .map { exchangeRate -> calculateRate(exchangeRate, state.value.value) }
    }

    private fun calculateRate(exchangeRate: ExchangeRate, value: Float): CalculatedExchangeRate {
        return CalculatedExchangeRate(
            exchangeRate = exchangeRate,
            calculatedValue = formatValue(exchangeRate.currencyRate * value)
        )
    }

    private fun formatValue(value: Float): Float {
        val pattern = "#.${0.rangeTo(settings.valuePrecision).joinToString("") { "#" }}"
        return DecimalFormat(pattern).format(value).toFloat()
    }

    companion object {
        const val LOADING_DELAY = 1000L
    }
}