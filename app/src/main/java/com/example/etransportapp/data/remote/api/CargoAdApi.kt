package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.ad.CargoAdCreateRequest
import com.example.etransportapp.data.model.ad.CargoAdResponse
import com.example.etransportapp.data.model.ad.CargoAdUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface CargoAdApi {

    @GET("api/CargoAd")
    suspend fun getAllCargoAds(): Response<List<CargoAdResponse>>

    @GET("api/CargoAd/{id}")
    suspend fun getCargoAdById(@Path("id") id: Int): Response<CargoAdResponse>

    @POST("api/CargoAd")
    suspend fun createCargoAd(@Body request: CargoAdCreateRequest): Response<CargoAdResponse>

    @DELETE("api/CargoAd/{id}")
    suspend fun deleteCargoAd(@Path("id") id: Int): Response<Unit>

    @PUT("api/CargoAd/{id}")
    suspend fun updateCargoAd(
        @Path("id") id: Int,
        @Body request: CargoAdUpdateRequest
    ): Response<Unit>

    @GET("api/CargoAd/by-customer/{customerId}")
    suspend fun getCargoAdsByCustomer(@Path("customerId") customerId: String): Response<List<CargoAdResponse>>

    @GET("api/CargoAd/by-type/{cargoType}")
    suspend fun getCargoAdsByType(@Path("cargoType") cargoType: String): Response<List<CargoAdResponse>>
}
