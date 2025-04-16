package com.example.etransportapp.presentation.ui.home.myAds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.LoadAd
import com.example.etransportapp.data.model.VehicleAd
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdViewModel
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdCard
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdViewModel
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdCard
import com.example.etransportapp.ui.theme.LightBlue

@Composable
fun MyAdsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loadAdViewModel: LoadAdViewModel,
    vehicleAdViewModel: VehicleAdViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Yük İlanları", "Araç İlanları")

    // Geçici olarak kullanıcı ID'si sabit, ileride dinamik alınabilir
    val userId = "username"
    val myLoadAds by loadAdViewModel.myLoadAds.collectAsState()
    val myVehicleAds by vehicleAdViewModel.myVehicleAds.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = LightBlue,
            contentColor = Color.White
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> LoadAdsList(myLoadAds)
            1 -> VehicleAdsList(myVehicleAds)
        }
    }
}

@Composable
fun LoadAdsList(loadAds: List<LoadAd>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(loadAds) { ad ->
            LoadAdCard(item = ad)
        }
    }
}


@Composable
fun VehicleAdsList(vehicleAds: List<VehicleAd>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(vehicleAds) { ad ->
            VehicleAdCard(item = ad) // Daha sonra bu composable'ı yazarsın
        }
    }
}