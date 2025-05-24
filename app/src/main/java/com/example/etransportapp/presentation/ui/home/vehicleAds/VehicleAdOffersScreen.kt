package com.example.etransportapp.presentation.ui.home.vehicleAds

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdOffersScreen(vehicleAdId: String, navController: NavHostController) {
    val vehicleOffers = listOf(
        Triple("Mehmet Yılmaz", "+90 555 123 45 67", "Mersin'den Frankfurt'a yüküm var."),
        Triple("Ayşe Kaya", "+90 544 987 65 43", "Irak-Polonya 3000£."),
        Triple(
            "Ali Demir",
            "+90 530 321 76 89",
            "Yarın sabaha Gebze'den Edirne'ye kuş yükü yüklememiz var, ilgilenir misiniz?"
        )
    )

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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            vehicleOffers.forEach { (name, phone, message) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text(text = name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date()
                                ), style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "Telefon: $phone",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                "Mail: deneme@gmail.com",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = message, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                navController.context.startActivity(intent)
                            }, modifier = Modifier.width(140.dp)) {
                                Text("İletişime Geç", color = RoseRed)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { /* Reddet */ },
                                colors = ButtonDefaults.buttonColors(containerColor = RoseRed),
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
