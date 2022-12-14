package com.example.currencycalculator

import android.app.Application
import com.example.currencycalculator.di.appModule
import com.example.currencycalculator.di.dataModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CurrencyCalculatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        startKoin {
            androidLogger()
            androidContext(this@CurrencyCalculatorApplication)
            modules(appModule, dataModule)
        }
    }
}