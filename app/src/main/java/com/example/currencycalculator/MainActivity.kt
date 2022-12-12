package com.example.currencycalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.data.remote.RemoteDataSource
import com.example.data.respositories.CurrencyRepository
import com.example.data.respositories.CurrencyRepositoryImpl
import com.example.data.local.LocalDataSourceImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository: CurrencyRepository = CurrencyRepositoryImpl(
            RemoteDataSource().exchangeRateService,
            LocalDataSourceImpl(applicationContext)
        )

        MainScope().launch {
            val latest = repository.getExchangeRates("USD")

            Log.d("Test", latest.toString())
        }
    }
}