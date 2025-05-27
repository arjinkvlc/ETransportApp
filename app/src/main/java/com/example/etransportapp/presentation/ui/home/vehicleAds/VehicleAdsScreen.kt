package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.components.OvalDropdownBar
import com.example.etransportapp.presentation.components.VehicleAdCard
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.VehicleTypeMapUtil

@Composable
fun VehicleAdsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: VehicleAdViewModel
) {
    val vehicles by viewModel.filteredVehicleAds.collectAsState()
    val selectedSortLabel by remember { derivedStateOf { viewModel.selectedSort } }
    val selectedFilterLabel by remember {
        derivedStateOf {
            if (viewModel.selectedFilter == "Tümü") "Tümü"
            else VehicleTypeMapUtil.vehicleTypeMap[viewModel.selectedFilter] ?: "Diğer"
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllVehicleAds()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OvalDropdownBar(
                label = "Sırala",
                options = listOf("Tümü", "En Yeni", "En Eski", "Taşıma Kapasitesi"),
                selectedOption = selectedSortLabel,
                onOptionSelected = { viewModel.selectedSort = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OvalDropdownBar(
                label = "Filtrele",
                options = listOf("Tümü") + VehicleTypeMapUtil.vehicleTypeLabels,
                selectedOption = selectedFilterLabel,
                onOptionSelected = { selectedLabel ->
                    viewModel.selectedFilter = if (selectedLabel == "Tümü") {
                        "Tümü"
                    } else {
                        VehicleTypeMapUtil.getEnumValueFromLabel(selectedLabel) ?: "Others"
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            Text(
                text = "Temizle X",
                color = if (viewModel.selectedFilter == "Tümü") Color.LightGray else RoseRed,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable {
                        viewModel.selectedFilter = "Tümü"
                    },
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(vehicles.asReversed()) { item ->
                VehicleAdCard(item) {
                    viewModel.selectedAd = item
                    navController.navigate(NavRoutes.VEHICLE_AD_DETAIL)
                }
            }
        }
    }
}
