package com.example.etransportapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoNamesApi {
    @GET("searchJSON")
    suspend fun searchCitiesByCountry(
        @Query("q") query: String,
        @Query("country") countryCode: String,
        @Query("maxRows") maxRows: Int = 10,
        @Query("featureClass") featureClass: String = "P",
        @Query("username") username: String
    ): GeoNamesResponse

    @GET("countryInfoJSON")
    suspend fun getAllCountries(
        @Query("username") username: String
    ): CountryResponse
}

data class GeoNamesResponse(val geonames: List<GeoPlace>)
data class GeoPlace(
    val name: String,
    val countryName: String,
    val lat: String,
    val lng: String
)
data class CountryResponse(val geonames: List<Country>)
data class Country(
    val countryName: String,
    val countryCode: String
)
