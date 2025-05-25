package com.example.etransportapp.data.model.offer

data class VehicleOfferResponse(
    val id: Int,
    val senderId: String,
    val receiverId: String,
    val vehicleAdId: Int,
    val vehicleAdTitle: String,
    val message: String,
    val status: String,
    val expiryDate: String,
    val createdDate: String
)

