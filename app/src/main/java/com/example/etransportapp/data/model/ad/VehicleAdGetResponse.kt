package com.example.etransportapp.data.model.ad

data class VehicleAdGetResponse(
    val id: Int,
    val title: String,
    val description: String,
    val carrierId: String,
    val carrierName: String?,
    val vehicleType: String,
    val country: String,
    val city: String,
    val capacity: Int,
    val createdDate: String,
    val adDate: String
)
