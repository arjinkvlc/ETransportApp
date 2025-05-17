package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.remote.model.HereRouteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HereApi {
    @GET("2/calculateroute.json")
    suspend fun calculateRoute(
        @Query("apiKey") apiKey: String,
        @Query("waypoint0") waypoint0: String,
        @Query("waypoint1") waypoint1: String,
        @Query("mode") mode: String = "fastest;car;traffic:enabled",
        @Query("tollVehicleType") tollType: Int = 6,
        @Query("vehicleWeight") weight: Int
    ): HereRouteResponse
}
