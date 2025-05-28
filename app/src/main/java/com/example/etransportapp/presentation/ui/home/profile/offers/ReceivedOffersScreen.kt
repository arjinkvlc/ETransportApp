package com.example.etransportapp.presentation.ui.home.profile.offers

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.components.LoadOfferCard
import com.example.etransportapp.presentation.components.VehicleOfferCard
import com.example.etransportapp.presentation.ui.home.profile.ProfileViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedOffersScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val vehicleOffers = viewModel.vehicleOffersReceived.collectAsState().value
    val cargoOffers = viewModel.cargoOffersReceived.collectAsState().value

    var selectedTabIndex by remember { mutableStateOf(0) }
    Log.d("ProfileViewModel", "vehicleOffers.size: ${vehicleOffers.size}, cargoOffers.size: ${cargoOffers.size}")

    val tabTitles = listOf("Araç Teklifleri", "Yük Teklifleri")

    LaunchedEffect(vehicleOffers) {
        vehicleOffers.forEach { offer ->
            viewModel.fetchUserInfoByUserId(offer.senderId)
        }
    }

    LaunchedEffect(cargoOffers) {
        vehicleOffers.forEach { offer ->
            viewModel.fetchUserInfoByUserId(offer.senderId)
        }
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
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = RoseRed,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = RoseRed
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        selectedContentColor = RoseRed,
                        unselectedContentColor = Color.Gray,
                        text = { Text(title) }
                    )
                }
            }


            when (selectedTabIndex) {
            0 -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vehicleOffers) { offer ->
                        val sender = viewModel.senderInfoMap[offer.senderId]
                        VehicleOfferCard(
                            offer = offer,
                            senderName = sender?.name,
                            senderSurname = sender?.surname,
                            senderPhone = sender?.phoneNumber,
                            senderEmail = sender?.email,
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }

            1 -> {
                LazyColumn (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    items(cargoOffers) { offer ->
                        val sender = viewModel.senderInfoMap[offer.senderId]
                        LoadOfferCard(
                            offer = offer,
                            senderName = sender?.name,
                            senderSurname = sender?.surname,
                            senderPhone = sender?.phoneNumber,
                            senderEmail = sender?.email,
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
    }
}
