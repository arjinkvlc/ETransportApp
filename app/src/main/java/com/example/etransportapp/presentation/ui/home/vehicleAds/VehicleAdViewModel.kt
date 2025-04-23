package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.ad.VehicleAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VehicleAdViewModel : ViewModel() {
    private val _vehicleAds = MutableStateFlow<List<VehicleAd>>(emptyList())
    val vehicleAds: StateFlow<List<VehicleAd>> = _vehicleAds

    private val _myVehicleAds = MutableStateFlow<List<VehicleAd>>(emptyList())
    val myVehicleAds: StateFlow<List<VehicleAd>> = _myVehicleAds

    var selectedAd: VehicleAd? = null

    fun addVehicleAd(item: VehicleAd) {
        _vehicleAds.value = _vehicleAds.value + item
        if (item.userId == "username") {
            _myVehicleAds.value = _myVehicleAds.value + item
        }
    }

    fun deleteAd(ad: VehicleAd) {
        _vehicleAds.value = _vehicleAds.value.filterNot { it == ad }
        _myVehicleAds.value = _myVehicleAds.value.filterNot { it == ad }
    }

    fun updateAd(updatedAd: VehicleAd) {
        _vehicleAds.value = _vehicleAds.value.map { if (it == selectedAd) updatedAd else it }
        _myVehicleAds.value = _myVehicleAds.value.map { if (it == selectedAd) updatedAd else it }
        selectedAd = updatedAd
    }
}
