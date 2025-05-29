package com.example.etransportapp.data.model.ad

data class CargoAdResponse(
    val id: Int,
    val userId: String,
    val customerName: String?,
    val title: String,
    val description: String,
    val dropCountry: String,
    val dropCity: String,
    val pickCountry: String,
    val pickCity: String,
    val currency: String,
    val weight: Int,
    val cargoType: String,
    val price: Int,
    val isExpired: Boolean,
    val createdDate: String,
    val adDate: String
)
