package com.example.data

import com.example.data.local.LocalDataSource
import com.example.data.models.Currency
import com.example.data.models.ExchangeRates
import com.example.data.models.exceptions.DeprecatedDataException
import com.example.data.models.exceptions.NoDataException
import com.example.data.remote.ExchangeRateService
import com.example.data.remote.models.ExchangeRatesRemote
import com.example.data.respositories.CurrencyRepositoryImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CurrencyRepositoryTests {

    private val localDataSource = mock(LocalDataSource::class.java)
    private val exchangeRateService = mock(ExchangeRateService::class.java)

    private val currencyRepository = CurrencyRepositoryImpl(
        exchangeRateService = exchangeRateService,
        localDataSource = localDataSource
    )

    @Test
    fun `GIVEN correct remote data WHEN get exchange rates THEN data returned`() {
        runBlocking {
            val remoteData = ExchangeRatesRemote(true, "USD", "2020-10-10", mapOf())
            val expected = ExchangeRates(Currency.USD, DateTime.parse("2020-10-10"), listOf())
            `when`(exchangeRateService.latest("USD")).thenReturn(remoteData)

            val exchangeRates = currencyRepository
                .getExchangeRates(Currency.USD)
                .flowOn(Dispatchers.IO)
                .first()

            assertEquals(expected, exchangeRates)
        }
    }

    @Test(expected = NoDataException::class)
    fun `GIVEN no remote data and empty local data WHEN get exchange rates THEN operation failed`() {
        runBlocking {
            `when`(exchangeRateService.latest("USD")).thenThrow(RuntimeException())
            `when`(localDataSource.getExchangeRates()).thenThrow(NoDataException())

            currencyRepository
                .getExchangeRates(Currency.USD)
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    @Test(expected = DeprecatedDataException::class)
    fun `GIVEN no remote data and deprecated local data WHEN get exchange rates THEN operation failed`() {
        runBlocking {
            val localData = ExchangeRates(Currency.USD, DateTime.now().minusDays(1), listOf())
            `when`(exchangeRateService.latest("USD")).thenThrow(RuntimeException())
            `when`(localDataSource.getExchangeRates()).thenReturn(flowOf(localData))

            currencyRepository
                .getExchangeRates(Currency.USD)
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    @Test(expected = NoDataException::class)
    fun `GIVEN no remote data and new base currency WHEN get exchange rates THEN operation failed`() {
        runBlocking {
            val localData = ExchangeRates(Currency.USD, DateTime.now(), listOf())
            `when`(exchangeRateService.latest("PLN")).thenThrow(RuntimeException())
            `when`(localDataSource.getExchangeRates()).thenReturn(flowOf(localData))

            currencyRepository
                .getExchangeRates(Currency.PLN)
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    @Test(expected = NoDataException::class)
    fun `GIVEN no unknown remote data base currency WHEN get exchange rates THEN operation failed`() {
        runBlocking {
            val remoteData = ExchangeRatesRemote(true, "XXX", "2020-10-10", mapOf())
            val localData = ExchangeRates(Currency.USD, DateTime.now(), listOf())
            `when`(exchangeRateService.latest("USD")).thenReturn(remoteData)
            `when`(localDataSource.getExchangeRates()).thenReturn(flowOf(localData))

            currencyRepository
                .getExchangeRates(Currency.USD)
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}