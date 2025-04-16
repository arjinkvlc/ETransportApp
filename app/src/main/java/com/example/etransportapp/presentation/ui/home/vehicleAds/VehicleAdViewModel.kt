package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.VehicleAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class VehicleAdViewModel : ViewModel() {
    private val _vehicleAds = MutableStateFlow<List<VehicleAd>>(emptyList())
    val vehicleAds: StateFlow<List<VehicleAd>> = _vehicleAds

    private val _myVehicleAds = MutableStateFlow<List<VehicleAd>>(emptyList())
    val myVehicleAds: StateFlow<List<VehicleAd>> = _myVehicleAds

    fun addVehicleAd(item: VehicleAd) {
        _vehicleAds.value = _vehicleAds.value + item
        if (item.userId == "username") {
            _myVehicleAds.value = _myVehicleAds.value + item
        }
    }
}

