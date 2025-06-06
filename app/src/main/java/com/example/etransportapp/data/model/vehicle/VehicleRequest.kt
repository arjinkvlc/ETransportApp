package com.example.etransportapp.data.model.vehicle

data class VehicleRequest(
    val id: Int,
    val carrierId: String,
    val title: String,
    val vehicleType: String,
    val capacity: Int,
    val licensePlate: String,
    val model: String
)
