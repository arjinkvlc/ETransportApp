package com.example.etransportapp.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.etransportapp.presentation.ui.home.loadAds.CreateLoadAdScreen
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdDetailScreen
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdOffersScreen
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdViewModel
import com.example.etransportapp.presentation.ui.home.loadAds.LoadAdsScreen
import com.example.etransportapp.presentation.ui.home.myAds.MyAdsScreen
import com.example.etransportapp.presentation.ui.home.profile.ProfileScreen
import com.example.etransportapp.presentation.ui.home.profile.myVehicles.MyVehiclesScreen
import com.example.etransportapp.presentation.ui.home.vehicleAds.CreateVehicleAdScreen
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdDetailScreen
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdOffersScreen
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdViewModel
import com.example.etransportapp.presentation.ui.home.vehicleAds.VehicleAdsScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.intro.IntroScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.login.ForgotPasswordScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.login.LoginScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.login.LoginViewModel
import com.example.etransportapp.presentation.ui.loginAndRegister.onboarding.OnboardingScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.register.RegisterScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.register.RegisterViewModel
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.util.PreferenceHelper

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: Context
) {
    val startDestination = when {
        PreferenceHelper.isFirstTime(context) -> NavRoutes.ONBOARDING
        PreferenceHelper.isLoggedIn(context) -> NavRoutes.LOAD_ADS
        else -> NavRoutes.INTRO
    }
    val loadAdViewModel: LoadAdViewModel = viewModel()
    val vehicleAdViewModel: VehicleAdViewModel = viewModel()
    val vehicleViewModel: VehicleViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.ONBOARDING) { OnboardingScreen(navController) }
        composable(NavRoutes.INTRO) { IntroScreen(modifier, navController) }
        composable(NavRoutes.LOGIN) { LoginScreen(LoginViewModel(), modifier, navController) }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                viewModel = RegisterViewModel(),
                navController = navController
            )
        }

        composable(NavRoutes.LOAD_ADS) { LoadAdsScreen(modifier, navController, loadAdViewModel) }
        composable(NavRoutes.VEHICLE_ADS) {
            VehicleAdsScreen(
                modifier,
                navController,
                vehicleAdViewModel
            )
        }
        composable(NavRoutes.MY_ADS) {
            MyAdsScreen(
                modifier,
                navController,
                loadAdViewModel,
                vehicleAdViewModel
            )
        }
        composable(NavRoutes.PROFILE) { ProfileScreen(modifier, navController) }
        composable(NavRoutes.MY_VEHICLES) {
            MyVehiclesScreen(navController = navController, vehicleViewModel = vehicleViewModel)
        }

        composable(NavRoutes.CREATE_LOAD_AD) {
            CreateLoadAdScreen(
                modifier,
                navController,
                loadAdViewModel
            )
        }
        composable(NavRoutes.CREATE_VEHICLE_AD) {
            CreateVehicleAdScreen(
                modifier,
                navController,
                vehicleAdViewModel,
                vehicleViewModel
            )
        }
        composable(NavRoutes.LOAD_AD_DETAIL) {
            val selectedAd = loadAdViewModel.selectedAd
            selectedAd?.let {
                LoadAdDetailScreen(
                    loadAd = it,
                    navController = navController,
                    onDeleteClick = {
                        loadAdViewModel.deleteAd(it)
                        navController.popBackStack()
                    },
                    onUpdateClick = { updatedAd ->
                        loadAdViewModel.updateAd(updatedAd)
                    }
                )
            }
        }

        composable("loadAdOffers/{loadAdId}") { backStackEntry ->
            val loadAdId = backStackEntry.arguments?.getString("loadAdId") ?: ""
            LoadAdOffersScreen(loadAdId = loadAdId, navController = navController)
        }

        composable(NavRoutes.VEHICLE_AD_DETAIL) {
            val selectedAd = vehicleAdViewModel.selectedAd
            selectedAd?.let {
                VehicleAdDetailScreen(
                    vehicleAd = it,
                    navController = navController,
                    onDeleteClick = {
                        vehicleAdViewModel.deleteAd(it)
                        navController.popBackStack()
                    },
                    onUpdateClick = { updatedAd ->
                        vehicleAdViewModel.updateAd(updatedAd)
                    }, vehicleViewModel = vehicleViewModel
                )
            }
        }
        composable("vehicleAdOffers/{vehicleAdId}") { backStackEntry ->
            val vehicleAdId = backStackEntry.arguments?.getString("vehicleAdId") ?: ""
            VehicleAdOffersScreen(vehicleAdId = vehicleAdId, navController = navController)
        }
        composable(NavRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController)
        }

    }
}
