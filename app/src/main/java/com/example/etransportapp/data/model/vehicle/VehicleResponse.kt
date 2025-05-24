package com.example.etransportapp.data.model.vehicle

data class VehicleResponse(
    val active: Boolean,
    val addedBy: String,
    val capacity: Int,
    val carrier: Any,
    val createdDate: String,
    val id: Int,
    val licensePlate: String,
    val model: String,
    val title: String,
    val updatedBy: Any,
    val updatedDate: Any,
    val userId: String,
    val vehicleType: String
)