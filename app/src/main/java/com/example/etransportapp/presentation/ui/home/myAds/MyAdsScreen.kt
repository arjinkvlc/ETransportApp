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
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.model.ad.VehicleAd
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdViewModel
import com.example.etransportapp.presentation.components.LoadAdCard
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdViewModel
import com.example.etransportapp.presentation.components.VehicleAdCard
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun MyAdsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loadAdViewModel: LoadAdViewModel,
    vehicleAdViewModel: VehicleAdViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Yük İlanları", "Araç İlanları")

    val myLoadAds by loadAdViewModel.myLoadAds.collectAsState()
    val myVehicleAds by vehicleAdViewModel.myVehicleAds.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = DarkGray,
            contentColor = Color.White,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                )
            }
        }

        when (selectedTabIndex) {
            0 -> LoadAdsList(myLoadAds) { selectedAd ->
                loadAdViewModel.selectedAd = selectedAd
                navController.navigate(NavRoutes.LOAD_AD_DETAIL)
            }

            1 -> VehicleAdsList(myVehicleAds) { selectedAd ->
                vehicleAdViewModel.selectedAd = selectedAd
                navController.navigate(NavRoutes.VEHICLE_AD_DETAIL)
            }
        }
    }
}

@Composable
fun LoadAdsList(loadAds: List<LoadAd>, onAdClick: (LoadAd) -> Unit) {
    //TODO : MOCK ÖRNEK İLAN DAHA SONRA KALDIRILACAK
    val mockLoadAdItem =
        LoadAd("Örnek Yük İlanı", "Örnek Açıklama", "Mersin", "Istanbul", cargoType = "Diğer","5000", currency = "USD","05/03/2001", weight = "20")
    LoadAdCard(item = mockLoadAdItem) {
        onAdClick(mockLoadAdItem)
    }
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
        items(loadAds) { ad ->
            LoadAdCard(item = ad, onClick = { onAdClick(ad) })
        }
    }
}


@Composable
fun VehicleAdsList(vehicleAds: List<VehicleAd>, onAdClick: (VehicleAd) -> Unit) {
    //TODO : MOCK ÖRNEK İLAN DAHA SONRA KALDIRILACAK
    val mockVehicleAdItem =
        VehicleAd(title = "Örnek Araç İlanı", description = "Örnek Açıklama", location = "Mersin", date = "05/03/2001", userId = "username",capacity = "20", cargoType = "Diğer")
    VehicleAdCard(item = mockVehicleAdItem) {
        onAdClick(mockVehicleAdItem)
    }

    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
        items(vehicleAds) { ad ->
            VehicleAdCard(item = ad, onClick = { onAdClick(ad) })
        }
    }
}