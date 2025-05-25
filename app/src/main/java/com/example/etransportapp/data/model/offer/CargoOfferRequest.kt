package com.example.etransportapp.data.model.offer

data class CargoOfferRequest(
    val senderId: String,
    val receiverId: String,
    val cargoAdId: Int,
    val price: Int,
    val message: String,
    val expiryDate: String
)

