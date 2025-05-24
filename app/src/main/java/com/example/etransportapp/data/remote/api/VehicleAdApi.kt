package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.ad.VehicleAdRequest
import com.example.etransportapp.data.model.ad.VehicleAdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VehicleAdApi {
    @POST("api/VehicleAd")
    suspend fun createVehicleAd(@Body request: VehicleAdRequest): Response<VehicleAdResponse>
}
