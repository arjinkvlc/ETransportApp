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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.components.LoadAdCard
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray

@Composable
fun LoadAdsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadAdViewModel
) {
    val loads by viewModel.loadAds.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
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
