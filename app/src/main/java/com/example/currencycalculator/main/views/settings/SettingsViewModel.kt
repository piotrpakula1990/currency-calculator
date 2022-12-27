package com.example.currencycalculator.main.views.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Currency
import com.example.data.respositories.settings.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.currencycalculator.main.views.settings.SettingsUiState.*
import com.example.currencycalculator.main.views.settings.SettingsAction.*
import timber.log.Timber

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _state = MutableStateFlow<SettingsUiState>(Initialize)
    val state: StateFlow<SettingsUiState> get() = _state

    init {
        Timber.d("Initialize view model ($this)")
        viewModelScope.launch {
            settingsRepository
                .getSettings()
                .onEach { settings ->
                    val result = Result(
                        baseCurrency = settings.baseCurrency,
                        valuePrecision = settings.valuePrecision,
                        valuePrecisionPreview = createPreviewValuePrecision(settings.valuePrecision),
                        orderPreview = createPreviewOrder(settings.order),
                        order = settings.order
                    )

                    _state.emit(result)
                }
                .collect()
        }
    }

    fun intent(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                is SetBaseCurrency -> {
                    Timber.i("Set base currency: ${action.currency}")
                    settingsRepository.setBaseCurrency(baseCurrency = action.currency)
                }
                is SetValuePrecision -> {
                    Timber.i("Set value precision: ${action.value}")
                    if (action.value > MAX_PRECISION_VALUE) return@launch
                    settingsRepository.setValuePrecision(valuePrecision = action.value)
                }
                is SetOrder -> {
                    Timber.i("Set order: ${action.order.joinToString()}")
                    settingsRepository.setCurrenciesOrder(order = action.order)
                }
            }
        }
    }

    private fun createPreviewValuePrecision(valuePrecision: Int): String {
        val zeros = (0 until valuePrecision - 1).joinToString("") { "0" }
        return "1.${zeros}1"
    }

    private fun createPreviewOrder(order: List<Currency>): String {
        val suffix = if (order.size > 4) ", ..." else ""
        return order.take(4).joinToString().plus(suffix)
    }

    companion object {
        const val MAX_PRECISION_VALUE = 6
    }
}