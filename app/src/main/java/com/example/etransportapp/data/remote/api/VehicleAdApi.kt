package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.ad.VehicleAdCreateRequest
import com.example.etransportapp.data.model.ad.VehicleAdCreateResponse
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.data.model.ad.VehicleAdUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehicleAdApi {
    @POST("api/VehicleAd")
    suspend fun createVehicleAd(@Body request: VehicleAdCreateRequest): Response<VehicleAdCreateResponse>

    @GET("api/VehicleAd")
    suspend fun getAllVehicleAds(): Response<List<VehicleAdGetResponse>>

    @DELETE("api/VehicleAd/{id}")
    suspend fun deleteVehicleAd(@Path("id") id: Int): retrofit2.Response<Unit>

    @PUT("api/VehicleAd/{id}")
    suspend fun updateVehicleAd(
        @Path("id") id: Int,
        @Body request: VehicleAdUpdateRequest
    ): retrofit2.Response<Unit>

}
