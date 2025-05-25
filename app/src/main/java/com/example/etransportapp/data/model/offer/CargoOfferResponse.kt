package com.example.etransportapp.data.model.offer

data class CargoOfferResponse(
    val id: Int,
    val senderId: String,
    val receiverId: String,
    val cargoAdId: Int,
    val cargoAdTitle: String,
    val price: Int,
    val message: String,
    val status: String,
    val expiryDate: String,
    val createdDate: String
)

