package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.vehicle.FetchUserVehiclesResponse
import com.example.etransportapp.data.model.vehicle.VehicleRequest
import com.example.etransportapp.data.model.vehicle.VehicleResponse
import retrofit2.Response
import retrofit2.http.*

interface VehicleApi {

    @GET("api/Vehicle/by-carrier/{carrierId}")
    suspend fun getVehiclesByUser(@Path("carrierId") carrierId: String): Response<List<FetchUserVehiclesResponse>>

    @POST("api/Vehicle")
    suspend fun addVehicle(@Body request: VehicleRequest): Response<VehicleResponse>

    @DELETE("api/Vehicle/{id}")
    suspend fun deleteVehicle(@Path("id") id: Int): Response<Unit>

    @PUT("api/Vehicle/{id}")
    suspend fun updateVehicle(
        @Path("id") id: Int,
        @Body request: VehicleRequest
    ): Response<Unit>

    @GET("api/Vehicle/{id}")
    suspend fun getVehicleById(@Path("id") id: Int): Response<FetchUserVehiclesResponse>


}
