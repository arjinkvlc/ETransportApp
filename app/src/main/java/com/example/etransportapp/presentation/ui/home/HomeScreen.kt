package com.example.etransportapp.presentation.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.etransportapp.presentation.navigation.NavGraph
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.LightBlue

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
    val showBottomBar = showTopBar
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
                        containerColor = LightBlue,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                val items = listOf("Yük İlanları", "Araç İlanları","İlanlarım", "Profil")
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
                    routes.forEachIndexed { index, route ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Image(
                                    painter = painterResource(id = icons[index]),
                                    contentDescription = items[index],
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(LightBlue)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(navController = navController, modifier = Modifier.padding(innerPadding), context = context)
    }
}
