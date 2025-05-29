package com.example.etransportapp.data.model.ad

data class CargoAdCreateRequest(
    val userId: String,
    val title: String,
    val description: String,
    val weight: Int,
    val cargoType: String,
    val dropCountry: String,
    val dropCity: String,
    val pickCountry: String,
    val pickCity: String,
    val currency: String,
    val price: Int,
    val adDate : String
)
