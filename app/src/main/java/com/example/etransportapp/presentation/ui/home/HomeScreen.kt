package com.example.etransportapp.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.etransportapp.R
import com.example.etransportapp.presentation.components.FloatingActionMenu
import com.example.etransportapp.presentation.navigation.BottomBarScreen
import com.example.etransportapp.presentation.navigation.NavGraph
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.presentation.ui.home.notifications.NotificationViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val context = LocalContext.current

    val notificationViewModel: NotificationViewModel = viewModel()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    LaunchedEffect(Unit) {
        notificationViewModel.fetchUnreadCount()
    }

    var showActionModal by remember { mutableStateOf(false) }

    val showTopBar = currentRoute in listOf(
        NavRoutes.LOAD_ADS,
        NavRoutes.VEHICLE_ADS,
        NavRoutes.MY_ADS,
        NavRoutes.PROFILE
    )

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
                    actions = {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Badge(
                                        containerColor = Color.Red
                                    ) {
                                        Text(
                                            text = unreadCount.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        ) {
                            IconButton(onClick = {
                                navController.navigate("notifications")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Bildirimler",
                                    tint = Color.White
                                )
                            }
                        }
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
                NavigationBar {
                    BottomBarScreen.all.forEach { screen ->
                        if (screen == BottomBarScreen.CenterAction) {
                            NavigationBarItem(
                                selected = false,
                                onClick = { showActionModal = !showActionModal },
                                icon = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = if (showActionModal) Icons.Filled.Close else Icons.Filled.AddCircle,
                                            contentDescription = "Aksiyonlar",
                                            modifier = Modifier.size(28.dp),
                                            tint = RoseRed
                                        )
                                        Text(
                                            text = screen.label,
                                            fontSize = 12.sp,
                                            color = if (currentRoute == screen.route) RoseRed else DarkGray
                                        )
                                    }

                                },
                                label = null
                            )
                        } else {
                            NavigationBarItem(
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (showActionModal) {
                                        showActionModal = false
                                    }
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
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
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = RoseRed,
                                    unselectedIconColor = DarkGray,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).background(Color(0xFFEEEEEE))) {
            NavGraph(navController = navController, modifier = Modifier, context = context)

            FloatingActionMenu(
                show = showActionModal,
                onDismiss = { showActionModal = false },
                onNavigate = {
                    navController.navigate(it)
                    showActionModal = false
                }
            )


        }
    }
}
