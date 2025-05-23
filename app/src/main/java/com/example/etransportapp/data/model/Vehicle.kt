package com.example.etransportapp.data.model

import java.util.UUID

data class Vehicle(
    val vehicleId: String = UUID.randomUUID().toString(),
    val name: String,
    val vehicleType: String,
    val capacity: Int,
    val plate: String,
    val model: String
)
