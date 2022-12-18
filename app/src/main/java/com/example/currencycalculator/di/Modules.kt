package com.example.currencycalculator.di

import com.example.currencycalculator.main.views.exchange.ExchangeRatesViewModel
import com.example.currencycalculator.main.views.settings.SettingsViewModel
import com.example.data.local.LocalDataSource
import com.example.data.local.LocalDataSourceImpl
import com.example.data.remote.RemoteDataSource
import com.example.data.respositories.CurrencyRepository
import com.example.data.respositories.CurrencyRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ExchangeRatesViewModel(get()) }
    viewModel { SettingsViewModel() }
}

val dataModule = module {
    single { RemoteDataSource().exchangeRateService }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }
}