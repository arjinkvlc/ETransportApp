package com.example.etransportapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VehicleViewModel : ViewModel() {
    private val _myVehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val myVehicles: StateFlow<List<Vehicle>> = _myVehicles

    fun addVehicle(vehicle: Vehicle) {
        _myVehicles.value = _myVehicles.value + vehicle
    }

    fun deleteVehicle(vehicle: Vehicle) {
        _myVehicles.value = _myVehicles.value - vehicle
    }

    fun updateVehicle(updated: Vehicle) {
        _myVehicles.value = _myVehicles.value.map {
            if (it.vehicleId == updated.vehicleId) updated else it
        }
    }

}
