package com.example.etransportapp.presentation.navigation

import com.example.etransportapp.R

sealed class BottomBarScreen(val route: String, val label: String, val icon: Int) {
    object LoadAds : BottomBarScreen(NavRoutes.LOAD_ADS, "Yük İlanları", R.drawable.ic_loadads)
    object VehicleAds : BottomBarScreen(NavRoutes.VEHICLE_ADS, "Araç İlanları", R.drawable.ic_vehicleads)
    object Profile : BottomBarScreen(NavRoutes.PROFILE, "Profil", R.drawable.baseline_person_24)

    companion object {
        val all = listOf(LoadAds, VehicleAds, Profile)
    }
}
