package com.example.currencycalculator

import android.app.Application
import com.example.currencycalculator.di.appModule
import com.example.currencycalculator.di.dataModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class CurrencyCalculatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeTimber()
        JodaTimeAndroid.init(this)

        startKoin {
            androidLogger()
            androidContext(this@CurrencyCalculatorApplication)
            modules(appModule, dataModule)
        }
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}