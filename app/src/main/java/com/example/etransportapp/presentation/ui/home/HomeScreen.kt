package com.example.etransportapp.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.etransportapp.R
import com.example.etransportapp.presentation.navigation.BottomBarScreen
import com.example.etransportapp.presentation.navigation.NavGraph
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showTopBar = currentRoute in listOf(
        NavRoutes.LOAD_ADS,
        NavRoutes.VEHICLE_ADS,
        NavRoutes.MY_ADS,
        NavRoutes.PROFILE
    )
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentRoute) {
                                NavRoutes.LOAD_ADS -> "Yük İlanları"
                                NavRoutes.VEHICLE_ADS -> "Araç İlanları"
                                NavRoutes.MY_ADS -> "İlanlarım"
                                NavRoutes.PROFILE -> "Profil"
                                else -> ""
                            },
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic,
                                fontSize = 24.sp
                            )
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = DarkGray,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (showTopBar) {
                val items = listOf("Yük İlanları", "Araç İlanları", "İlanlarım", "Profil")
                val icons = listOf(
                    R.drawable.ic_loadads,
                    R.drawable.ic_vehicleads,
                    R.drawable.ic_myads,
                    R.drawable.baseline_person_24
                )
                val routes = listOf(
                    NavRoutes.LOAD_ADS,
                    NavRoutes.VEHICLE_ADS,
                    NavRoutes.MY_ADS,
                    NavRoutes.PROFILE
                )

                NavigationBar {
                    BottomBarScreen.all.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = screen.label,
                                        modifier = Modifier.size(28.dp),
                                        tint = if (currentRoute == screen.route) RoseRed else DarkGray
                                    )
                                    Text(
                                        text = screen.label,
                                        fontSize = 12.sp,
                                        color = if (currentRoute == screen.route) RoseRed else DarkGray
                                    )
                                }
                            },
                            alwaysShowLabel = false,

                        )
                    }
                }

            }
        }
    ) { innerPadding ->
        NavGraph(navController = navController, modifier = Modifier.padding(innerPadding), context = context)
    }
}
