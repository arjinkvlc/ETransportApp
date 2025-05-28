package com.example.etransportapp.presentation.ui.home.profile

import RetrofitInstance.cargoOfferApi
import RetrofitInstance.vehicleOfferApi
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.model.offer.CargoOfferResponse
import com.example.etransportapp.data.model.offer.VehicleOfferResponse
import com.example.etransportapp.data.model.offer.VehicleOfferStatusUpdateRequest
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

    private val _senderInfoMap = mutableStateMapOf<String, UserProfileResponse>()
    val senderInfoMap: Map<String, UserProfileResponse> = _senderInfoMap


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

            Log.d("ProfileViewModel", "vehicleReceived: ${vehicleReceived.code()} - ${vehicleReceived.body()?.size}")
            Log.d("ProfileViewModel", "cargoReceived: ${cargoReceived.code()} - ${cargoReceived.body()?.size}")

            if (cargoSent.isSuccessful) {
                _cargoOffersSent.value = cargoSent.body() ?: emptyList()
            }
            if (cargoReceived.isSuccessful) {
                _cargoOffersReceived.value = cargoReceived.body() ?: emptyList()
            }
        }


    }

    fun fetchUserInfoByUserId(userId: String) {
        viewModelScope.launch {
            if (_senderInfoMap.contains(userId)) return@launch
            try {
                val response = RetrofitInstance.userApi.getUserProfile(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _senderInfoMap[userId] = it
                    }
                }
            } catch (e: Exception) {
                println("Kullanıcı bilgisi alınamadı: ${e.localizedMessage}")
            }
        }
    }

    fun cancelOffer(
        offerId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val request = VehicleOfferStatusUpdateRequest(
                    offerId = offerId,
                    status = "Cancelled"
                )

                val response = RetrofitInstance.vehicleOfferApi.updateVehicleOfferStatus(
                    id = offerId,
                    request = request
                )

                if (response.isSuccessful) {
                    _vehicleOffersSent.value = _vehicleOffersSent.value.map {
                        if (it.id == offerId) it.copy(status = "Cancelled") else it
                    }
                    _vehicleOffersReceived.value = _vehicleOffersReceived.value.map {
                        if (it.id == offerId) it.copy(status = "Cancelled") else it
                    }
                    onSuccess()
                } else {
                    onError("Hata kodu: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("İstisna: ${e.localizedMessage}")
            }
        }
    }



}
