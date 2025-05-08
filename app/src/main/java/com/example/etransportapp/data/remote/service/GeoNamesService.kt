package com.example.etransportapp.data.remote.service

import com.example.etransportapp.data.remote.api.GeoNamesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeoNamesService {
    private const val BASE_URL = "https://secure.geonames.org/"

    fun create(): GeoNamesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoNamesApi::class.java)
    }
}
