package com.example.currencycalculator

import com.example.currencycalculator.main.views.settings.SettingsAction
import com.example.currencycalculator.main.views.settings.SettingsUiState
import com.example.currencycalculator.main.views.settings.SettingsViewModel
import com.example.data.models.Currency.*
import com.example.data.models.Settings
import com.example.data.respositories.settings.SettingsRepository
import com.example.data.respositories.settings.SettingsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class SettingsViewModelTests {

    private val settingsRepository: SettingsRepository = mock(SettingsRepositoryImpl::class.java)

    private lateinit var viewModel: SettingsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        Dispatchers.resetMain()
    }


    @Test
    fun `GIVEN currency PLN WHEN set base currency THEN result state returned`() {
        runBlocking {
            val settingsFlow = MutableStateFlow(defaultSettings)
            `when`(settingsRepository.getSettings()).thenReturn(settingsFlow)
            `when`(settingsRepository.setBaseCurrency(PLN))
                .then {
                    runBlocking { settingsFlow.emit(defaultSettings.copy(baseCurrency = PLN)) }
                }
            viewModel = SettingsViewModel(settingsRepository = settingsRepository)
            delay(10)

            viewModel.intent(SettingsAction.SetBaseCurrency(PLN))
            delay(10)

            val result = viewModel.state.value as SettingsUiState.Result
            assertEquals(PLN, result.baseCurrency)
        }
    }

    @Test
    fun `GIVEN value 5 WHEN set value precision THEN result state returned`() {
        runBlocking {
            val settingsFlow = MutableStateFlow(defaultSettings)
            `when`(settingsRepository.getSettings()).thenReturn(settingsFlow)
            `when`(settingsRepository.setValuePrecision(5))
                .then {
                    runBlocking { settingsFlow.emit(defaultSettings.copy(valuePrecision = 5)) }
                }
            viewModel = SettingsViewModel(settingsRepository = settingsRepository)
            delay(10)

            viewModel.intent(SettingsAction.SetValuePrecision(5))
            delay(10)

            val result = viewModel.state.value as SettingsUiState.Result
            assertEquals(5, result.valuePrecision)
        }
    }

    @Test
    fun `GIVEN value 7 WHEN set value precision THEN error state returned`() {
        runBlocking {
            val settingsFlow = MutableStateFlow(defaultSettings)
            `when`(settingsRepository.getSettings()).thenReturn(settingsFlow)
            viewModel = SettingsViewModel(settingsRepository = settingsRepository)
            delay(10)

            viewModel.intent(SettingsAction.SetValuePrecision(7))
            delay(10)

            val result = viewModel.state.value as SettingsUiState.Result
            verify(settingsRepository, times(0)).setValuePrecision(7)
            assertEquals(defaultSettings.valuePrecision, result.valuePrecision)
        }
    }

    @Test
    fun `GIVEN valid input WHEN set order THEN result state returned`() {
        runBlocking {
            val order = listOf(GEL, GBP, PLN)
            val settingsFlow = MutableStateFlow(defaultSettings)
            `when`(settingsRepository.getSettings()).thenReturn(settingsFlow)
            `when`(settingsRepository.setCurrenciesOrder(order))
                .then {
                    runBlocking { settingsFlow.emit(defaultSettings.copy(order = order)) }
                }
            viewModel = SettingsViewModel(settingsRepository = settingsRepository)
            delay(10)

            viewModel.intent(SettingsAction.SetOrder(order))
            delay(10)

            val result = viewModel.state.value as SettingsUiState.Result
            assertEquals(order, result.order)
        }
    }

    companion object {
        private val defaultSettings = Settings(EUR, 2, listOf(USD, EUR, GBP, PLN))
    }
}