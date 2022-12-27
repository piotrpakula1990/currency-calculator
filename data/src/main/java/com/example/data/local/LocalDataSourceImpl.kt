package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.models.Currency
import com.example.data.models.ExchangeRates
import com.example.data.models.Settings
import com.example.data.models.exceptions.NoDataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalDataSourceImpl(private val applicationContext: Context) : LocalDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "exchange_rates_data_store")

    override suspend fun setExchangeRates(exchangeRates: ExchangeRates) {
        applicationContext.dataStore.edit { preferences ->
            preferences[EXCHANGE_RATES] = Json.encodeToString(exchangeRates)
        }
    }

    override fun getExchangeRates(): Flow<ExchangeRates> {
        return applicationContext.dataStore.data.map { preferences ->
            preferences[EXCHANGE_RATES]
                ?.let { Json.decodeFromString(it) }
                ?: throw NoDataException()
        }
    }

    override suspend fun setBaseCurrency(currency: Currency) {
        applicationContext.dataStore.edit { preferences ->
            preferences[BASE_CURRENCY] = currency.name
        }
    }

    override suspend fun setValuePrecision(precision: Int) {
        applicationContext.dataStore.edit { preferences ->
            preferences[VALUE_PRECISION] = precision
        }
    }

    override suspend fun setCurrenciesOrder(order: List<Currency>) {
        applicationContext.dataStore.edit { preferences ->
            preferences[ORDER] = Json.encodeToString(order.map { it.name })
        }
    }

    override fun getSettings(): Flow<Settings> {
        return applicationContext.dataStore.data.map { preferences ->

            val baseCurrency = preferences[BASE_CURRENCY]
                ?.let { Currency.getOrNull(it) }
                ?: DEFAULT_CURRENCY

            val valuePrecision = preferences[VALUE_PRECISION] ?: DEFAULT_VALUE_PRECISION

            val order = preferences[ORDER]
                ?.let { json -> Json.decodeFromString<List<String>>(json) }
                ?.mapNotNull { Currency.getOrNull(it) }
                ?: DEFAULT_ORDER

            Settings(
                baseCurrency = baseCurrency,
                valuePrecision = valuePrecision,
                order = order
            )
        }
    }

    companion object {
        private val EXCHANGE_RATES = stringPreferencesKey("exchange_rates")
        private val BASE_CURRENCY = stringPreferencesKey("base_currency")
        private val VALUE_PRECISION = intPreferencesKey("value_precision")
        private val ORDER = stringPreferencesKey("order")

        private const val DEFAULT_VALUE_PRECISION = 2
        private val DEFAULT_CURRENCY = Currency.EUR
        private val DEFAULT_ORDER = listOf(Currency.EUR, Currency.USD, Currency.GBP, Currency.CHF)
    }
}