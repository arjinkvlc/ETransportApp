package com.example.etransportapp.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.offer.VehicleOfferResponse
import com.example.etransportapp.presentation.ui.home.profile.ProfileViewModel
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun VehicleOfferCard(
    offer: VehicleOfferResponse,
    senderName: String?,
    senderSurname: String?,
    senderPhone: String?,
    senderEmail: String?,
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val isCancelled = offer.status == "Cancelled"
    val isPending = offer.status == "Pending"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = "${senderName.orEmpty()} ${senderSurname.orEmpty()}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = offer.createdDate.substring(0, 10),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(
                    text = "Telefon: ${senderPhone ?: "Bilinmiyor"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Mail: ${senderEmail ?: "Bilinmiyor"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Araç: ${offer.vehicleAdTitle}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))


            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Mesaj: ${offer.message}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (offer.status) {
                    "Pending" -> "Durum: Beklemede"
                    "Accepted" -> "Durum: Kabul Edildi"
                    "Rejected" -> "Durum: Reddedildi"
                    "Cancelled" -> "Durum: İptal Edildi"
                    else -> "Durum: ${offer.status}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        senderPhone?.let {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
                            navController.context.startActivity(intent)
                        }
                    },
                    enabled = !senderPhone.isNullOrEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("İletişime Geç", color = RoseRed)
                }

                Button(
                    onClick = {
                        viewModel.cancelLoadOffer(
                            offerId = offer.id,
                            onSuccess = { /* StateFlow günceller */ },
                            onError = { /* Hata gösterilebilir */ }
                        )
                    },
                    enabled = !isCancelled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCancelled) Color.Gray else RoseRed
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reddet", color = Color.White)
                }
            }
        }
    }
}

