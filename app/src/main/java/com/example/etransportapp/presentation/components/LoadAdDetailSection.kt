package com.example.etransportapp.presentation.components

import androidx.compose.runtime.Composable

@Composable
fun LoadAdDetailSection(
    title: String,
    description: String,
    origin: String,
    destination: String,
    weight: String,
    price: String,
    currency: String,
    date: String
) {
    InfoText("Başlık", title)
    InfoText("Yükleme Noktası", origin)
    InfoText("Varış Noktası", destination)
    InfoText("Yük Ağırlığı", "$weight ton")
    InfoText("Fiyat", "$price $currency")
    InfoText("Tarih", date)
    InfoText("Açıklama", description)
}
