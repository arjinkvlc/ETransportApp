package com.example.etransportapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("v6/latest/{base}")
    suspend fun getRates(@Path("base") base: String): CurrencyResponse
}

data class CurrencyResponse(
    val result: String,
    val baseCode: String,
    val rates: Map<String, Double>
)
