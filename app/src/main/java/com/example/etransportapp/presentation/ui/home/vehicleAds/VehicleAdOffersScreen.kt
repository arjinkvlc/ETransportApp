package com.example.etransportapp.presentation.ui.home.vehicleAds

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdOffersScreen(
    vehicleAdId: String,
    navController: NavHostController,
    vehicleAdViewModel: VehicleAdViewModel = viewModel()
) {
    val offers = vehicleAdViewModel.vehicleAdOffers
    val senderInfoMap = vehicleAdViewModel.senderInfoMap

    LaunchedEffect(vehicleAdId) {
        vehicleAdViewModel.fetchVehicleOffersByVehicleAdId(vehicleAdId.toInt())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gelen Teklifler", color = Color.White) },
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
                    vehicleAdViewModel.fetchUserInfo(offer.senderId)
                }

                val sender = senderInfoMap[offer.senderId]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
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
/*
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Teklif: ${offer.price} USD",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )*/

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mesaj: ${offer.message}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Durum: ${offer.status}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))
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
                                    vehicleAdViewModel.cancelVehicleOffer(
                                        offerId = offer.id,
                                        onSuccess = { /* Güncellenecek */ },
                                        onError = { /* Hata gösterilebilir */ }
                                    )
                                },
                                enabled = !isCancelled,
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
