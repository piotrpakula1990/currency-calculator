package com.example.currencycalculator.main.views.settings

import com.example.data.models.Currency

data class SettingsUiState(
    val defaultBaseCurrency: Currency,
    val valuePrecision: Int,
    val order: List<Currency>
)

sealed class SettingsAction {
    class SetBaseCurrency(val currency: Currency) : SettingsAction()
    class SetValuePrecision(val value: Int) : SettingsAction()
    class SetOrder(val list: List<Currency>) : SettingsAction()
}