package com.example.data.respositories.settings

import com.example.data.local.LocalDataSource
import com.example.data.models.Settings
import com.example.data.models.Currency
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val localDataSource: LocalDataSource
) : SettingsRepository {

    override fun getSettings(): Flow<Settings> {
        return localDataSource.getSettings()
    }

    override suspend fun setBaseCurrency(baseCurrency: Currency) {
        return localDataSource.setBaseCurrency(baseCurrency)
    }

    override suspend fun setValuePrecision(valuePrecision: Int) {
        return localDataSource.setValuePrecision(valuePrecision)
    }

    override suspend fun setCurrenciesOrder(order: List<Currency>) {
        return localDataSource.setCurrenciesOrder(order)
    }
}