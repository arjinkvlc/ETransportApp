package com.example.etransportapp.data.model.ad

data class VehicleAdCreateRequest(
    val title: String,
    val description: String,
    val country: String,
    val city: String,
    val carrierId: String,
    val vehicleType: String,
    val capacity: Int
)
