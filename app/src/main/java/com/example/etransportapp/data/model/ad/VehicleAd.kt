package com.example.etransportapp.data.model.ad

data class VehicleAd(
    val id: String = "",
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val capacity: String,
    val cargoType: String,
    val userId: String = "username",
    val isListing: Boolean = true,
)