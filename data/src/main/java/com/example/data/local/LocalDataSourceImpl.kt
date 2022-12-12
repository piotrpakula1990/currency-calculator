package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.models.ExchangeRates
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

    companion object {
        private val EXCHANGE_RATES = stringPreferencesKey("exchange_rates")
    }
}