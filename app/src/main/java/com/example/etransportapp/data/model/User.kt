package com.example.etransportapp.data.model

import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.model.ad.VehicleAd

data class User(
    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val ownedTrucks: List<Vehicle>,
    val vehicleAds: List<VehicleAd>,
    val loadAds: List<LoadAd>,
)
