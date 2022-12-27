package com.example.data.respositories.currency

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
import timber.log.Timber

class CurrencyRepositoryImpl(
    private val exchangeRateService: ExchangeRateService,
    private val localDataSource: LocalDataSource
) : CurrencyRepository {

    override fun getExchangeRates(baseCurrency: Currency): Flow<ExchangeRates> {
        return flow {
            try {
                val exchangeRates = exchangeRateService.latest(baseCurrency.name).asExchangeRates()
                localDataSource.setExchangeRates(exchangeRates)

                Timber.d("Success at get remote exchange rates.")
                emit(exchangeRates)
            } catch (t : Throwable) {
                Timber.w("Error at get remote data: $t")
                val exchangeRates : ExchangeRates = localDataSource.getExchangeRates().first()

                when {
                    t is MappingException -> throw NoDataException()
                    isWrongBaseCurrency(baseCurrency, exchangeRates) -> throw NoDataException()
                    isExchangeRatesDeprecated(exchangeRates) -> throw DeprecatedDataException()
                    else -> {
                        Timber.d("Success at get local(cached) exchange rates.")
                        emit(exchangeRates)
                    }
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