package com.example.etransportapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.etransportapp.R
import com.example.etransportapp.data.model.ad.VehicleAd
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.util.VehicleTypeMapUtil

@Composable
fun VehicleAdCard(item: VehicleAdGetResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(item.adDate.substring(0, 10), style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(Modifier.height(1.dp))
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Konum:", "${item.city}, ${item.country}")
                    InfoRow("Araç Türü:", VehicleTypeMapUtil.getLabelFromEnumValue(item.vehicleType))
                    InfoRow("Kapasite:", "${item.capacity} Ton")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = CircleShape,
                    color = DarkGray,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.carrierName?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value)
    }
}
