package com.example.currencycalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.data.api.Retrofit
import com.example.data.respositories.CurrencyRepository
import com.example.data.respositories.CurrencyRepositoryImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository: CurrencyRepository = CurrencyRepositoryImpl(Retrofit())

        MainScope().launch {
            val latest = repository.getExchangeCurrencyRates("USD")

            Log.d("Test", latest.success.toString())
        }
    }
}