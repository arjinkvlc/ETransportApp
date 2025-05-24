package com.example.etransportapp.data.model.vehicle

data class FetchUserVehiclesResponse(
    val id: Int,
    val licensePlate: String,
    val capacity: Int,
    val model: String,
    val title: String,
    val carrierId: String,
    val carrierName: String,
    val vehicleType: String
)
