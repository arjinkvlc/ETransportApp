package com.example.etransportapp.presentation.components

import androidx.compose.runtime.Composable
import com.example.etransportapp.util.VehicleTypeMapUtil

@Composable
fun LoadAdDetailSection(
    title: String,
    description: String,
    origin: String,
    destination: String,
    weight: String,
    price: String,
    currency: String,
    date: String,
    type: String
) {
    InfoText("Başlık", title)
    InfoText("Yükleme Noktası", origin)
    InfoText("Varış Noktası", destination)
    InfoText("Yük Tipi", VehicleTypeMapUtil.getLabelFromEnumValue(type))
    InfoText("Yük Ağırlığı", "$weight ton")
    InfoText("Fiyat", "$price $currency")
    InfoText("Tarih", date)
    InfoText("Açıklama", description)
}
