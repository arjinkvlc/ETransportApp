package com.example.etransportapp.data.model

import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.model.ad.VehicleAd

data class User(
    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val ownedVehicles: List<Vehicle> = emptyList(),
    val vehicleAds: List<VehicleAd> = emptyList(),
    val loadAds: List<LoadAd> = emptyList()
)
