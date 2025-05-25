package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.offer.CargoOfferRequest
import com.example.etransportapp.data.model.offer.CargoOfferResponse
import com.example.etransportapp.data.model.offer.CargoOfferStatusUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface CargoOfferApi {

    @POST("api/CargoOffer")
    suspend fun createCargoOffer(@Body request: CargoOfferRequest): Response<CargoOfferResponse>

    @GET("api/CargoOffer")
    suspend fun getAllOffers(): Response<List<CargoOfferResponse>>

    @PUT("api/CargoOffer/{id}/status")
    suspend fun updateOfferStatus(
        @Path("id") offerId: Int,
        @Body request: CargoOfferStatusUpdateRequest
    ): Response<Void>

    @GET("api/CargoOffer/{id}")
    suspend fun getOfferById(@Path("id") offerId: Int): Response<CargoOfferResponse>

    @GET("api/CargoOffer/sender/{senderId}")
    suspend fun getOffersBySender(@Path("senderId") senderId: String): Response<List<CargoOfferResponse>>

    @GET("api/CargoOffer/receiver/{receiverId}")
    suspend fun getOffersByReceiver(@Path("receiverId") receiverId: String): Response<List<CargoOfferResponse>>

    @GET("api/CargoOffer/cargo-ad/{cargoAdId}")
    suspend fun getOffersByCargoAd(@Path("cargoAdId") cargoAdId: Int): Response<List<CargoOfferResponse>>

    @GET("api/CargoOffer/pending/{userId}")
    suspend fun getPendingOffers(@Path("userId") userId: String): Response<List<CargoOfferResponse>>
}
