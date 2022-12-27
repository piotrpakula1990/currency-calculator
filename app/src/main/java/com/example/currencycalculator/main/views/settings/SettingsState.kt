package com.example.currencycalculator.main.views.settings

import com.example.data.models.Currency

sealed class SettingsUiState(
    open val baseCurrency: Currency = Currency.UNK,
    open val valuePrecision: Int = 0,
    open val valuePrecisionPreview: String = "",
    open val orderPreview: String = ""
) {
    object Initialize : SettingsUiState()

    data class Result(
        override val baseCurrency: Currency,
        override val valuePrecision: Int,
        override val valuePrecisionPreview: String,
        override val orderPreview: String,
        val order: List<Currency>
    ) : SettingsUiState()
}

sealed class SettingsAction {
    class SetBaseCurrency(val currency: Currency) : SettingsAction()
    class SetValuePrecision(val value: Int) : SettingsAction()
    class SetOrder(val order: List<Currency>) : SettingsAction()
}