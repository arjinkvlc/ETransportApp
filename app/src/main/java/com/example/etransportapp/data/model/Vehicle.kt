package com.example.etransportapp.data.model

data class Vehicle(
    val vehicleId: String,
    val name: String,
    val vehicleType: String,//listOf<String>("Tır", "Kırkayak","Kamyon")
    val trailerType: String,//listOf<String>("Açık Kasa","Kapalı Kasa", "Tenteli","Damperli","Tanker","Havuzlu Dorse","Frigofirik","Lowbed")
)
