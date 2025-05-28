package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.offer.CargoOfferResponse
import com.example.etransportapp.presentation.ui.home.profile.ProfileViewModel

@Composable
fun LoadOfferCard(
    offer: CargoOfferResponse,
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Yük: ${offer.cargoAdTitle}", fontWeight = FontWeight.SemiBold)
            Text("Fiyat: ${offer.price} USD")
            Text("Mesaj: ${offer.message}")
            Text("Tarih: ${offer.createdDate.substring(0, 10)}")
            Text("Durum: ${offer.status}")
        }
    }
}
