package com.example.etransportapp.data.model.ad

data class CargoAdPriceSuggestionResponse(
    val price: PriceDetail,
    val distance: Double,
    val duration: String,
    val origin: String,
    val destination: String
)

data class PriceDetail(
    val prediction: Double,
    val minPrice: Double,
    val maxPrice: Double
)

