package com.example.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit.Builder

@OptIn(ExperimentalSerializationApi::class)
class RemoteDataSource {

    private val retrofit = Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    var exchangeRateService: ExchangeRateService = retrofit.create(ExchangeRateService::class.java)

    companion object {
        private const val baseUrl: String = "https://api.exchangerate.host"

        private val contentType: MediaType = MediaType.get("application/json")
        private val json = Json { ignoreUnknownKeys = true }
    }
}