package com.example.etransportapp.data.remote.service

import com.example.etransportapp.data.remote.api.HereApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HereService {
    private const val BASE_URL = "https://fleet.ls.hereapi.com/"

    fun create(): HereApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HereApi::class.java)
    }
}
