package com.example.etransportapp.data.model.ad

data class CargoAdUpdateRequest(
    val id: Int,
    val title: String,
    val description: String,
    val weight: Int,
    val cargoType: String,
    val price: Int,
    val isExpired: Boolean,
    val dropCountry: String,
    val dropCity: String,
    val pickCountry: String,
    val pickCity: String,
    val currency: String
)
