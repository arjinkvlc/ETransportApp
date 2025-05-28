package com.example.etransportapp.presentation.components

import androidx.compose.runtime.Composable
import com.example.etransportapp.util.VehicleTypeMapUtil

@Composable
fun VehicleAdDetailSection(
    title: String,
    description: String,
    cargoType: String,
    capacity: String,
    location: String,
    date: String,
) {
    InfoText("Başlık", title)
    InfoText("Açıklama", description)
    InfoText("Araç Türü", VehicleTypeMapUtil.getLabelFromEnumValue(cargoType))
    InfoText("Taşıma Kapasitesi", "$capacity ton")
    InfoText("Konum", location)
    InfoText("Tarih", date)
}
