package com.example.etransportapp.data.model.ad

data class VehicleAdResponse(
    val capacity: Int,
    val carrierId: String,
    val carrierName: Any,
    val city: String,
    val country: String,
    val createdDate: String,
    val description: String,
    val id: Int,
    val title: String,
    val vehicleType: String
)