package com.example.etransportapp.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${senderName.orEmpty()} ${senderSurname.orEmpty()}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = offer.createdDate.substring(0, 10),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.wrapContentWidth()
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Telefon: ${senderPhone ?: "Bilinmiyor"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mail: ${senderEmail ?: "Bilinmiyor"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "İlan Başlığı: ${offer.vehicleAdTitle}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Mesaj: ${offer.message}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
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
                        viewModel.cancelVehicleOffer(
                            offerId = offer.id,
                            onSuccess = { /* State güncellenir */ },
                            onError = { /* Hata gösterilir */ }
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
