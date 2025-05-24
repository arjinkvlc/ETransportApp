package com.example.etransportapp.data.model.ad

data class VehicleAdUpdateRequest(
    val id: Int,
    val title: String,
    val description: String,
    val country: String,
    val city: String,
    val vehicleType: String,
    val capacity: Int
)
