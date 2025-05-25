package com.example.etransportapp.util

object VehicleTypeMapUtil {
    val vehicleTypeMap = mapOf(
        "TarpaulinTruck" to "Tenteli Tır",
        "BoxTruck" to "Kasa Kamyon",
        "RefrigeratedTruck" to "Soğutuculu Tır",
        "SemiTrailer" to "Dorse",
        "LightTruck" to "Hafif Kamyonet",
        "ContainerCarrier" to "Konteyner Taşıyıcı",
        "TankTruck" to "Tanker",
        "LowbedTrailer" to "Alçak Dorseli Römork",
        "DumpTruck" to "Damperli Kamyon",
        "PanelVan" to "Panelvan",
        "Others" to "Diğer"
    )

    fun getEnumValueFromLabel(label: String): String? {
        return vehicleTypeMap.entries.find { it.value == label }?.key
    }

    val vehicleTypeLabels = vehicleTypeMap.values.toList()
}
