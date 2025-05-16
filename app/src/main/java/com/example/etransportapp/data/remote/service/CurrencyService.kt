package com.example.etransportapp.data.remote.service

import com.example.etransportapp.data.remote.api.CurrencyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyService {
    private const val BASE_URL = "https://open.er-api.com/"

    fun create(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
}
