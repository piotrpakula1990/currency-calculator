package com.example.currencycalculator

import com.example.currencycalculator.main.views.exchange.ExchangeRatesAction
import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel
import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel.Companion.DEFAULT_CURRENCY
import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel.Companion.LOADING_DELAY
import com.example.data.models.Currency
import com.example.data.models.ExchangeRate
import com.example.data.models.ExchangeRates
import com.example.data.models.exceptions.NoDataException
import com.example.data.respositories.CurrencyRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.joda.time.DateTime
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ExchangeRatesViewModelTests {

    private val currencyRepository = mock(CurrencyRepositoryImpl::class.java)

    private lateinit var viewModel: ExchangeRatesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.IO)

        // init default data
        val exchangeRates = ExchangeRates(Currency.EUR, DateTime.now(), listOf())
        `when`(currencyRepository.getExchangeRates(DEFAULT_CURRENCY))
            .thenReturn(flowOf(exchangeRates))

        viewModel = ExchangeRatesViewModel(currencyRepository = currencyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN repository data WHEN set currency THEN success state returned`() {
        runBlocking {
            val rates = listOf(ExchangeRate(Currency.USD, 2f))
            val exchangeRates = ExchangeRates(Currency.EUR, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(Currency.EUR)).thenReturn(flowOf(exchangeRates))

            viewModel.intent(ExchangeRatesAction.SetCurrency(Currency.EUR))
            delay(LOADING_DELAY + 100)

            assertEquals(Currency.USD, viewModel.state.value.outputs.first().currency)
            assertEquals(2f, viewModel.state.value.outputs.first().calculatedValue)
            assertFalse(viewModel.state.value.isEmpty)
            assertFalse(viewModel.state.value.isLoading)
            assertNull(viewModel.state.value.error)
        }
    }

    @Test
    fun `GIVEN repository error WHEN set currency THEN error state returned`() {
        runBlocking {
            `when`(currencyRepository.getExchangeRates(Currency.PLN)).thenThrow(NoDataException())

            viewModel.intent(ExchangeRatesAction.SetCurrency(Currency.PLN))
            delay(LOADING_DELAY + 100)

            assertNotNull(viewModel.state.value.error)
            assertTrue(viewModel.state.value.outputs.isEmpty())
        }
    }

    @Test
    fun `GIVEN repository data WHEN set currency value THEN success state returned`() {
        runBlocking {
            val rates = listOf(ExchangeRate(Currency.USD, 2f))
            val exchangeRates = ExchangeRates(DEFAULT_CURRENCY, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(DEFAULT_CURRENCY))
                .thenReturn(flowOf(exchangeRates))

            viewModel.intent(ExchangeRatesAction.SetValue(2f))
            delay(LOADING_DELAY + 100)

            assertEquals(Currency.USD, viewModel.state.value.outputs.first().currency)
            assertEquals(4f, viewModel.state.value.outputs.first().calculatedValue)
            assertFalse(viewModel.state.value.isEmpty)
            assertFalse(viewModel.state.value.isLoading)
            assertNull(viewModel.state.value.error)
        }
    }

    @Test
    fun `GIVEN repository error WHEN set currency value THEN error state returned`() {
        runBlocking {
            `when`(currencyRepository.getExchangeRates(DEFAULT_CURRENCY)).thenThrow(NoDataException())

            viewModel.intent(ExchangeRatesAction.SetValue(2f))
            delay(LOADING_DELAY + 100)

            println(viewModel.state.value)
            assertNotNull(viewModel.state.value.error)
            assertTrue(viewModel.state.value.outputs.isEmpty())
        }
    }

    @Test
    fun `GIVEN nothing WHEN refresh data THEN state set to loading before finally result`() {
        runBlocking {
            val rates = listOf(ExchangeRate(Currency.USD, 2f))
            val exchangeRates = ExchangeRates(Currency.EUR, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(Currency.EUR)).thenReturn(flowOf(exchangeRates))

            viewModel.intent(ExchangeRatesAction.SetCurrency(Currency.EUR))
            assertTrue(viewModel.state.value.isLoading)

            delay(LOADING_DELAY + 100)
            assertFalse(viewModel.state.value.isLoading)
        }
    }
}