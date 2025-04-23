package com.example.etransportapp.data.model.ad

data class LoadAd(
    val title: String,
    val description: String,
    val origin: String,
    val destination: String,
    val price: String,
    val date: String,
    val userId: String = "username"
)