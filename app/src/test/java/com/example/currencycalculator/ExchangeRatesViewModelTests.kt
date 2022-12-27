package com.example.currencycalculator

import com.example.currencycalculator.main.views.exchange.ExchangeRatesAction
import com.example.currencycalculator.main.views.exchange.ExchangeRatesUiState
import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel
import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel.Companion.LOADING_DELAY
import com.example.data.models.Currency.*
import com.example.data.models.ExchangeRate
import com.example.data.models.ExchangeRates
import com.example.data.models.Settings
import com.example.data.models.exceptions.NoDataException
import com.example.data.respositories.currency.CurrencyRepositoryImpl
import com.example.data.respositories.settings.SettingsRepositoryImpl
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
    private val settingsRepository = mock(SettingsRepositoryImpl::class.java)

    private lateinit var viewModel: ExchangeRatesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.IO)

        runBlocking { // init default data
            val rates = listOf(ExchangeRate(USD, 2f))
            val exchangeRates = ExchangeRates(EUR, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(EUR))
                .thenReturn(flowOf(exchangeRates))

            val settings = Settings(EUR, 2, listOf(USD, EUR, GBP, PLN))
            `when`(settingsRepository.getSettings()).thenReturn(flowOf(settings))

            viewModel = ExchangeRatesViewModel(
                currencyRepository = currencyRepository,
                settingsRepository = settingsRepository
            )

            delay(LOADING_DELAY + 10) // wait until initialize reload is done.
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN repository data WHEN set currency THEN result state returned`() {
        runBlocking {
            val rates = listOf(ExchangeRate(USD, 2f))
            val exchangeRates = ExchangeRates(EUR, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(EUR)).thenReturn(flowOf(exchangeRates))

            viewModel.intent(ExchangeRatesAction.SetCurrency(EUR))
            delay(LOADING_DELAY + 10)

            val result = viewModel.state.value as ExchangeRatesUiState.Result
            assertEquals(USD, result.outputs.first().exchangeRate.currency)
            assertEquals(2f, result.outputs.first().calculatedValue)
        }
    }

    @Test
    fun `GIVEN repository error WHEN set currency THEN error state returned`() {
        runBlocking {
            `when`(currencyRepository.getExchangeRates(PLN)).thenThrow(NoDataException())

            viewModel.intent(ExchangeRatesAction.SetCurrency(PLN))
            delay(LOADING_DELAY + 10)

            val error = viewModel.state.value as ExchangeRatesUiState.Error
            assertNotNull(error.error)
        }
    }

    @Test
    fun `GIVEN value 2 WHEN set currency value THEN result state returned`() {
        runBlocking {
            viewModel.intent(ExchangeRatesAction.SetValue(2f))
            delay(10)

            val result = viewModel.state.value as ExchangeRatesUiState.Result
            assertEquals(USD, result.outputs.first().exchangeRate.currency)
            assertEquals(4f, result.outputs.first().calculatedValue)
        }
    }

    @Test
    fun `GIVEN any input WHEN set currency THEN state set to loading before finally result`() {
        runBlocking {
            val rates = listOf(ExchangeRate(EUR, 2f))
            val exchangeRates = ExchangeRates(USD, DateTime.now(), rates)
            `when`(currencyRepository.getExchangeRates(USD)).thenReturn(flowOf(exchangeRates))

            viewModel.intent(ExchangeRatesAction.SetCurrency(USD))
            delay(10)
            assertTrue(viewModel.state.value is ExchangeRatesUiState.Loading)

            delay(LOADING_DELAY + 100)
            assertFalse(viewModel.state.value is ExchangeRatesUiState.Loading)
        }
    }
}