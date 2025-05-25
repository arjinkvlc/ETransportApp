package com.example.etransportapp.presentation.ui.home.myAds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
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
    val myVehicleAds by vehicleAdViewModel.vehicleAds.collectAsState() // tüm ilanlar çekiliyor

    val filteredMyVehicleAds = myVehicleAds.filter {
        it.carrierId == "username" // veya userId kontrolü yap
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = DarkGray,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp),
                    color = RoseRed
                )
            },
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, fontSize =if (selectedTabIndex == index) 16.sp else 12.sp, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium) },
                )
            }
        }

        when (selectedTabIndex) {
            0 -> LoadAdsList(myLoadAds) { selectedAd ->
                loadAdViewModel.selectedAd = selectedAd
                navController.navigate(NavRoutes.LOAD_AD_DETAIL)
            }

            1 -> VehicleAdsList(filteredMyVehicleAds) { selectedAd ->
                vehicleAdViewModel.selectedAd = selectedAd
                navController.navigate(NavRoutes.VEHICLE_AD_DETAIL)
            }
        }
    }
}

@Composable
fun LoadAdsList(loadAds: List<LoadAd>, onAdClick: (LoadAd) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
        items(loadAds) { ad ->
            LoadAdCard(item = ad, onClick = { onAdClick(ad) })
        }
    }
}

@Composable
fun VehicleAdsList(vehicleAds: List<VehicleAdGetResponse>, onAdClick: (VehicleAdGetResponse) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
        items(vehicleAds) { ad ->
            VehicleAdCard(item = ad, onClick = { onAdClick(ad) })
        }
    }
}
