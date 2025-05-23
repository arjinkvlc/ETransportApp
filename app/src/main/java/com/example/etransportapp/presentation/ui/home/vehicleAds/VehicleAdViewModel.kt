package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")
    val sortedLoadAds = derivedStateOf {
        when (selectedSort) {
            "En Yeni" -> vehicleAds.value.sortedBy {it.date }
            "En Eski" -> vehicleAds.value.sortedByDescending { it.date }
            "Taşıma Kapasitesi" -> vehicleAds.value.sortedBy{ it.capacity }
            else -> vehicleAds.value
        }
    }
    val filteredLoadAds = derivedStateOf {
        when (selectedFilter) {
            "Açık Kasa" -> vehicleAds.value.filter { it.cargoType == "Açık Kasa" }
            "Tenteli" -> vehicleAds.value.filter { it.cargoType == "Tenteli"  }
            "Frigofirik" -> vehicleAds.value.filter { it.cargoType == "Frigofirik"  }
            "Tanker" -> vehicleAds.value.filter { it.cargoType == "Tanker"  }
            else -> vehicleAds.value
        }
    }

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
