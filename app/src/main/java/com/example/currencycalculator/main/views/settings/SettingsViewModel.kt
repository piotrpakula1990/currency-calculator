package com.example.currencycalculator.main.views.settings

import androidx.lifecycle.ViewModel
import com.example.data.models.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel()  {

    private val statePrivate = MutableStateFlow(getDefaultState())
    val state: StateFlow<SettingsUiState> get() = statePrivate

    fun intent(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetBaseCurrency -> {}
            is SettingsAction.SetValuePrecision -> {}
            is SettingsAction.SetOrder -> {}
        }
    }

    // todo prepare repository
    private fun getDefaultState() = SettingsUiState(
        defaultBaseCurrency = Currency.USD,
        valuePrecision = 1,
        order = listOf(Currency.EUR, Currency.USD, Currency.PLN)
    )
}