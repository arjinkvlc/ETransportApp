package com.example.etransportapp.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.etransportapp.presentation.ui.home.loadAds.CreateLoadScreen
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdViewModel
import com.example.etransportapp.presentation.ui.home.loadAds.LoadsScreen
import com.example.etransportapp.presentation.ui.home.profile.ProfileScreen
import com.example.etransportapp.presentation.ui.home.truckAds.VehicleAdsScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.intro.IntroScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.login.LoginScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.login.LoginViewModel
import com.example.etransportapp.presentation.ui.loginAndRegister.register.RegisterScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.register.RegisterViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: Context
) {
    val startDestination = if (isUserLoggedIn(context)) NavRoutes.LOAD_ADS else NavRoutes.INTRO
    val loadAdViewModel: LoadAdViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOAD_ADS) { LoadsScreen(modifier, navController,loadAdViewModel) }
        composable(NavRoutes.VEHICLE_ADS) { VehicleAdsScreen(modifier, navController) }
        composable(NavRoutes.PROFILE) { ProfileScreen(modifier, navController) }
        composable(NavRoutes.CREATE_LOAD_AD) { CreateLoadScreen(modifier, navController,loadAdViewModel) }
        composable(NavRoutes.INTRO) { IntroScreen(modifier, navController) }
        composable(NavRoutes.LOGIN) { LoginScreen(LoginViewModel(), modifier, navController) }
        composable(NavRoutes.REGISTER) { RegisterScreen(RegisterViewModel(), modifier, navController) }
    }
}


fun isUserLoggedIn(context: Context): Boolean {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("is_logged_in", false)
}
