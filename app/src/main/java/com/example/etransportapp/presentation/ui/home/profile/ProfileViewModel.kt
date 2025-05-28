package com.example.etransportapp.presentation.ui.home.profile

import RetrofitInstance.cargoOfferApi
import RetrofitInstance.vehicleOfferApi
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.model.offer.CargoOfferResponse
import com.example.etransportapp.data.model.offer.VehicleOfferResponse
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile = _userProfile.asStateFlow()

    // Araç teklifleri
    private val _vehicleOffersSent = MutableStateFlow<List<VehicleOfferResponse>>(emptyList())
    val vehicleOffersSent: StateFlow<List<VehicleOfferResponse>> = _vehicleOffersSent

    private val _vehicleOffersReceived = MutableStateFlow<List<VehicleOfferResponse>>(emptyList())
    val vehicleOffersReceived: StateFlow<List<VehicleOfferResponse>> = _vehicleOffersReceived

    // Yük teklifleri
    private val _cargoOffersSent = MutableStateFlow<List<CargoOfferResponse>>(emptyList())
    val cargoOffersSent: StateFlow<List<CargoOfferResponse>> = _cargoOffersSent

    private val _cargoOffersReceived = MutableStateFlow<List<CargoOfferResponse>>(emptyList())
    val cargoOffersReceived: StateFlow<List<CargoOfferResponse>> = _cargoOffersReceived


    fun fetchUserProfile(context: Context) {
        val userId = PreferenceHelper.getUserId(context) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.userApi.getUserProfile(userId)
            if (response.isSuccessful) {
                _userProfile.value = response.body()
            }
        }
    }
    fun fetchAllOffers(userId: String) {
        viewModelScope.launch {
            // Araç teklifleri
            val vehicleSent = vehicleOfferApi.getVehicleOffersBySenderId(userId)
            val vehicleReceived = vehicleOfferApi.getVehicleOffersByReceiverId(userId)

            if (vehicleSent.isSuccessful) {
                _vehicleOffersSent.value = vehicleSent.body() ?: emptyList()
            }
            if (vehicleReceived.isSuccessful) {
                _vehicleOffersReceived.value = vehicleReceived.body() ?: emptyList()
            }

            // Yük teklifleri
            val cargoSent = cargoOfferApi.getOffersBySender(userId)
            val cargoReceived = cargoOfferApi.getOffersByReceiver(userId)

            if (cargoSent.isSuccessful) {
                _cargoOffersSent.value = cargoSent.body() ?: emptyList()
            }
            if (cargoReceived.isSuccessful) {
                _cargoOffersReceived.value = cargoReceived.body() ?: emptyList()
            }
        }
    }


}
