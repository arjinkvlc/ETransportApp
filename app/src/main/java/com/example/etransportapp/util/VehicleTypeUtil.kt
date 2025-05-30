package com.example.etransportapp.util

object VehicleTypeMapUtil {
    val vehicleTypeMap = mapOf(
        "General" to "Tenteli Tır",
        "Fragile" to "Kasa Kamyon",
        "Refrigerated" to "Soğutuculu Tır",
        "Oversized" to "Dorse",
        "LightFreight" to "Hafif Kamyonet",
        "Containerized" to "Konteyner Taşıyıcı",
        "Liquid" to "Tanker",
        "HeavyMachinery" to "Alçak Dorseli Römork",
        "Construction" to "Damperli Kamyon",
        "Parcel" to "Panelvan",
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
