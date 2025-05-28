package com.example.etransportapp.data.model.ad

data class CargoAdPriceSuggestionRequest(
    val cargoType: String,
    val pickCity: String,
    val pickCountry: String,
    val deliveryCity: String,
    val deliveryCountry: String,
    val weight: Double
)
