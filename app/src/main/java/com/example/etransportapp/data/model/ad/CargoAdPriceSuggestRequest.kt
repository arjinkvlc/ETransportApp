package com.example.etransportapp.data.model.ad

data class CargoAdPriceSuggestionRequest(
    val pickCountry: String,
    val pickCity: String,
    val dropCountry: String,
    val dropCity: String,
    val weight: Double,
    val cargoType: String,
)
