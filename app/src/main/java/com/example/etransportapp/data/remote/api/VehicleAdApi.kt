package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.ad.VehicleAdCreateRequest
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.data.model.ad.VehicleAdUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleAdApi {
    @POST("api/VehicleAd")
    suspend fun createVehicleAd(@Body request: VehicleAdCreateRequest): Response<VehicleAdGetResponse>

    @GET("api/VehicleAd")
    suspend fun getAllVehicleAds(@Query("status") status: Int = 1): Response<List<VehicleAdGetResponse>>

    @GET("api/VehicleAd/by-carrier/{id}")
    suspend fun getVehicleAdsByCarrierId(
        @Path("id") carrierId: String,
    ): Response<List<VehicleAdGetResponse>>

    @DELETE("api/VehicleAd/{id}")
    suspend fun deleteVehicleAd(@Path("id") id: Int): Response<Unit>

    @PUT("api/VehicleAd/{id}")
    suspend fun updateVehicleAd(
        @Path("id") id: Int,
        @Body request: VehicleAdUpdateRequest
    ): Response<Unit>

}
