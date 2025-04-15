package com.example.etransportapp.presentation.navigation

import com.example.etransportapp.R

sealed class BottomBarScreen(val route: String, val label: String, val icon: Int) {
    object Loads : BottomBarScreen(NavRoutes.LOADS, "Yük İlanları", R.drawable.icon_yukbul)
    object Trucks : BottomBarScreen(NavRoutes.TRUCKS, "Araç İlanları", R.drawable.icon_tirbul)
    object Profile : BottomBarScreen(NavRoutes.PROFILE, "Profil", R.drawable.baseline_person_24)

    companion object {
        val all = listOf(Loads, Trucks, Profile)
    }
}
