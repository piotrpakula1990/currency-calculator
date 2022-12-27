package com.example.data.respositories.settings

import com.example.data.models.Settings
import com.example.data.models.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun setBaseCurrency(baseCurrency: Currency)

    suspend fun setValuePrecision(valuePrecision: Int)

    suspend fun setCurrenciesOrder(order: List<Currency>)
}