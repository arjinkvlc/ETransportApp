package com.example.etransportapp.data.model.offer

data class VehicleOfferRequest(
    val senderId: String,
    val receiverId: String,
    val vehicleAdId: Int,
    val message: String,
    val expiryDate: String
)

