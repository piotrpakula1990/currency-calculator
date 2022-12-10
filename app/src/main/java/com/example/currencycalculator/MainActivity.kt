package com.example.currencycalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.data.TestData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val testData = TestData()
    }
}