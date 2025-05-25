package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.offer.VehicleOfferRequest
import com.example.etransportapp.data.model.offer.VehicleOfferResponse
import com.example.etransportapp.data.model.offer.VehicleOfferStatusUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface VehicleOfferApi {

    @POST("api/VehicleOffer")
    suspend fun createVehicleOffer(@Body request: VehicleOfferRequest): Response<VehicleOfferResponse>

    @GET("api/VehicleOffer")
    suspend fun getAllVehicleOffers(): Response<List<VehicleOfferResponse>>

    @PUT("api/VehicleOffer/{id}/status")
    suspend fun updateVehicleOfferStatus(
        @Path("id") id: Int,
        @Body request: VehicleOfferStatusUpdateRequest
    ): Response<Unit>

    @GET("api/VehicleOffer/{id}")
    suspend fun getVehicleOfferById(@Path("id") id: Int): Response<VehicleOfferResponse>

    @GET("api/VehicleOffer/sender/{senderId}")
    suspend fun getVehicleOffersBySenderId(@Path("senderId") senderId: String): Response<List<VehicleOfferResponse>>

    @GET("api/VehicleOffer/receiver/{receiverId}")
    suspend fun getVehicleOffersByReceiverId(@Path("receiverId") receiverId: String): Response<List<VehicleOfferResponse>>

    @GET("api/VehicleOffer/vehicle-ad/{vehicleAdId}")
    suspend fun getVehicleOffersByVehicleAdId(@Path("vehicleAdId") vehicleAdId: Int): Response<List<VehicleOfferResponse>>

    @GET("api/VehicleOffer/pending/{userId}")
    suspend fun getPendingVehicleOffersByUserId(@Path("userId") userId: String): Response<List<VehicleOfferResponse>>
}
