package com.example.etransportapp.presentation.ui.home.profile.offers

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.components.LoadOfferCard
import com.example.etransportapp.presentation.components.VehicleOfferCard
import com.example.etransportapp.presentation.ui.home.profile.ProfileViewModel
import com.example.etransportapp.ui.theme.DarkGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentOffersScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel()
) {
    val vehicleOffers = viewModel.vehicleOffersSent.collectAsState().value
    val cargoOffers = viewModel.cargoOffersSent.collectAsState().value

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabTitles = listOf("Araç Teklifleri", "Yük Teklifleri")

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
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                LazyColumn {
                    items(vehicleOffers) { offer ->
                        VehicleOfferCard(offer = offer, navController = navController, viewModel = viewModel)
                    }
                }
            }

            1 -> {
                LazyColumn {
                    items(cargoOffers) { offer ->
                        LoadOfferCard(offer = offer, navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
    }
}

