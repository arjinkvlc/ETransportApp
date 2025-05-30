package com.example.etransportapp.presentation.ui.home.vehicleAds

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = DarkGray)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(offers.value) { offer ->
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${sender?.name.orEmpty()} ${sender?.surname.orEmpty()}",
                                style = MaterialTheme.typography.titleMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = offer.createdDate.substring(0, 10),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.wrapContentWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Telefon: ${sender?.phoneNumber ?: "Bilinmiyor"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mail: ${sender?.email ?: "Bilinmiyor"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mesaj: ${offer.message}",
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 5
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (offer.status == "Pending") "Durum: Beklemede" else "Durum: Reddedildi",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val phone = sender?.phoneNumber
                                    if (!phone.isNullOrBlank()) {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                        navController.context.startActivity(intent)
                                    }
                                },
                                enabled = sender?.phoneNumber != null,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("İletişime Geç", color = RoseRed)
                            }

                            val isCancelled = offer.status == "Cancelled"

                            Button(
                                onClick = {
                                    vehicleAdViewModel.cancelVehicleOffer(
                                        offerId = offer.id,
                                        onSuccess = { /* opsiyonel geri bildirim */ },
                                        onError = { /* opsiyonel hata gösterimi */ }
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
        }
    }
}
