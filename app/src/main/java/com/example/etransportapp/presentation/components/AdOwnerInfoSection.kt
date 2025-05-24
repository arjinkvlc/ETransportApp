package com.example.etransportapp.presentation.components

import androidx.compose.runtime.Composable

@Composable
fun AdOwnerInfoSection(name: String, email: String, phone: String) {
    InfoText("Ad Soyad", name)
    InfoText("E-Posta", email)
    InfoText("Telefon", phone)
}
