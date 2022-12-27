package com.example.currencycalculator.di

import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel
import com.example.currencycalculator.main.views.settings.SettingsViewModel
import com.example.data.local.LocalDataSource
import com.example.data.local.LocalDataSourceImpl
import com.example.data.remote.RemoteDataSource
import com.example.data.respositories.currency.CurrencyRepository
import com.example.data.respositories.currency.CurrencyRepositoryImpl
import com.example.data.respositories.settings.SettingsRepository
import com.example.data.respositories.settings.SettingsRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ExchangeRatesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}

val dataModule = module {
    single { RemoteDataSource().exchangeRateService }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}