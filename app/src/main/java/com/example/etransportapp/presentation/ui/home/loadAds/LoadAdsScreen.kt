package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.components.LoadAdCard
import com.example.etransportapp.presentation.components.OvalDropdownBar
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun LoadAdsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadAdViewModel
) {
    val loads by viewModel.loadAds.collectAsState()
    val selectedSort = viewModel.selectedSort
    val selectedFilter = viewModel.selectedFilter

    LaunchedEffect(Unit) {
        viewModel.fetchAllCargoAds()
    }


    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            OvalDropdownBar(
                label = "Sırala",
                options = listOf("Tümü", "En Yeni","En Eski", "Ucuzdan Pahalıya", "Pahalıdan Ucuza"),
                selectedOption = selectedSort,
                onOptionSelected = { viewModel.selectedSort = it },
                modifier = Modifier
                    .padding( vertical = 8.dp)
            )
            OvalDropdownBar(
                label = "Filtrele",
                options = listOf("Tümü", "Yurtiçi", "Uluslararası"),
                selectedOption = selectedFilter,
                onOptionSelected = { viewModel.selectedFilter = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
            if (selectedFilter == "Tümü") {
                Text(
                    text = "Temizle X",
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(top = 24.dp),
                    fontWeight = FontWeight.Bold

                )
            } else {
                Text(
                    text = "Temizle X",
                    color = RoseRed,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .clickable { viewModel.selectedFilter = "Tümü" },
                    fontWeight = FontWeight.Bold
                )
            }
        }
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 16.dp),)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(loads) { item ->
                LoadAdCard(item) {
                    viewModel.selectedAd = item
                    navController.navigate(NavRoutes.LOAD_AD_DETAIL)
                }
            }
        }

    }
}
