package com.example.etransportapp.util

object VehicleTypeMapUtil {
    val vehicleTypeMap = mapOf(
        "Tarpaulin Truck" to "Tenteli Tır",
        "Box Truck" to "Kasa Kamyon",
        "Refrigerated Truck" to "Soğutuculu Tır",
        "Semi-Trailer" to "Dorse",
        "Light Truck" to "Hafif Kamyonet",
        "Container Carrier" to "Konteyner Taşıyıcı",
        "Tank Truck" to "Tanker",
        "Lowbed Trailer" to "Alçak Dorseli Römork",
        "Dump Truck" to "Damperli Kamyon",
        "Panel Van" to "Panelvan",
        "Others" to "Diğer"
    )

    fun getEnumValueFromLabel(label: String): String? {
        return vehicleTypeMap.entries.find { it.value == label }?.key
    }

    fun getLabelFromEnumValue(enumValue: String): String {
        return vehicleTypeMap[enumValue] ?: "Diğer"
    }


    val vehicleTypeLabels = vehicleTypeMap.values.toList()
}
