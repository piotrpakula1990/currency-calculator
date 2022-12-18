package com.example.data.respositories

import com.example.data.local.LocalDataSource
import com.example.data.remote.ExchangeRateService
import com.example.data.models.ExchangeRates
import com.example.data.mappers.asExchangeRates
import com.example.data.models.Currency
import com.example.data.models.exceptions.DeprecatedDataException
import com.example.data.models.exceptions.MappingException
import com.example.data.models.exceptions.NoDataException
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime

class CurrencyRepositoryImpl(
    private val exchangeRateService: ExchangeRateService,
    private val localDataSource: LocalDataSource
) : CurrencyRepository {

    override fun getExchangeRates(baseCurrency: Currency): Flow<ExchangeRates> {
        return flow {
            try {
                val exchangeRates = exchangeRateService.latest(baseCurrency.name).asExchangeRates()
                localDataSource.setExchangeRates(exchangeRates)
                emit(exchangeRates)
            } catch (t : Throwable) {
                val exchangeRates : ExchangeRates = localDataSource.getExchangeRates().first()

                when {
                    t is MappingException -> throw NoDataException()
                    isWrongBaseCurrency(baseCurrency, exchangeRates) -> throw NoDataException()
                    isExchangeRatesDeprecated(exchangeRates) -> throw DeprecatedDataException()
                    else -> emit(exchangeRates)
                }
            }
        }
    }

    private fun isExchangeRatesDeprecated(exchangeRates: ExchangeRates): Boolean {
        return exchangeRates.date.plusDays(1) < DateTime.now()
    }

    private fun isWrongBaseCurrency(baseCurrency: Currency, exchangeRates: ExchangeRates): Boolean {
        return exchangeRates.baseCurrency != baseCurrency
    }
}