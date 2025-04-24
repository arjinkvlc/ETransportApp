package com.example.etransportapp.presentation.navigation

import com.example.etransportapp.R

sealed class BottomBarScreen(val route: String, val label: String, val icon: Int) {
    object LoadAds : BottomBarScreen(NavRoutes.LOAD_ADS, "Yük İlanları", R.drawable.ic_loadads)
    object VehicleAds : BottomBarScreen(NavRoutes.VEHICLE_ADS, "Araç İlanları", R.drawable.ic_vehicleads)
    object CenterAction : BottomBarScreen("center_action", "İlan Oluştur", R.drawable.baseline_add_24)

    object MyAds : BottomBarScreen(NavRoutes.MY_ADS, "İlanlarım", R.drawable.ic_myads)
    object Profile : BottomBarScreen(NavRoutes.PROFILE, "Profil", R.drawable.baseline_person_24)

    companion object {
        val all = listOf(LoadAds, VehicleAds,CenterAction,MyAds, Profile)
    }
}
