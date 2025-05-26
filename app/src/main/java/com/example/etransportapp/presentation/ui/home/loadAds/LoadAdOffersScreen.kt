package com.example.etransportapp.presentation.ui.home.loadAds

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadAdOffersScreen(
    loadAdId: String,
    navController: NavHostController
) {

    val viewModel: LoadAdViewModel = viewModel()
    val offers = viewModel.cargoAdOffers
    val senderInfoMap = viewModel.senderInfoMap

    LaunchedEffect(loadAdId) {
        viewModel.fetchOffersByCargoAdId(loadAdId.toInt())
    }

    /*val offers = listOf(
        Triple("Mert Yıldız", "+90 545 123 45 67", "8000 TL"),
        Triple("Elif Deniz", "+90 544 654 32 10", "7500 TL"),
        Triple("Ahmet Kara", "+90 532 987 65 43", "7900 TL")
    )*/

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gelen Yük Teklifleri", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = DarkGray)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            offers.value.forEach { offer ->
                LaunchedEffect(offer.senderId) {
                    viewModel.fetchUserInfo(offer.senderId)
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    val sender = senderInfoMap[offer.senderId]

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text(
                                text = "${sender?.name.orEmpty()} ${sender?.surname.orEmpty()}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = offer.createdDate.substring(0, 10),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "Telefon: ${sender?.phoneNumber ?: "Bilinmiyor"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Mail: ${sender?.email ?: "Bilinmiyor"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Teklif: ${offer.price} ${"USD"}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mesaj: ${offer.message}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if(offer.status == "Pending") "Durum: Beklemede" else "Durum: Reddedildi",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val phone = sender?.phoneNumber
                                    if (!phone.isNullOrBlank()) {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                        navController.context.startActivity(intent)
                                    }
                                },
                                enabled = sender?.phoneNumber != null,
                                modifier = Modifier.width(140.dp)
                            ) {
                                Text("İletişime Geç", color = RoseRed)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            val isCancelled = offer.status == "Cancelled"

                            Button(
                                onClick = {
                                    viewModel.cancelOffer(
                                        offerId = offer.id,
                                        onSuccess = {
                                            // UI otomatik güncellenecek çünkü offers StateFlow
                                        },
                                        onError = {
                                            // Hata mesajı gösterilebilir
                                        }
                                    )
                                },
                                enabled = !isCancelled, // Cancel edilmişse pasif hale getir
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCancelled) Color.Gray else RoseRed
                                ),
                                modifier = Modifier.width(140.dp)
                            ) {
                                Text("Reddet", color = Color.White)
                            }

                        }
                    }
                }

            }
        }
    }
}

