package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.LightBlue

@Composable
fun LoadsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadAdViewModel
) {
    val loads by viewModel.loads.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(loads) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.description)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${item.origin} ➝ ${item.destination}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${item.price} ₺ | ${item.date}", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("create_load_ad") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(72.dp), // Daha büyük ve yuvarlak
            containerColor = LightBlue,
            shape = CircleShape // Tam yuvarlak form
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Yeni Yük İlanı Ekle",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

    }
}
