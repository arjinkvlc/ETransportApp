package com.example.etransportapp.data.model.ad

data class LoadAd(
    val title: String,
    val description: String,
    val origin: String,
    val destination: String,
    val cargoType: String,
    val price: String,
    val currency: String = "TRY",
    val date: String,
    val weight: String,
    val userId: String = "username",
    val isListing: Boolean = true,
)