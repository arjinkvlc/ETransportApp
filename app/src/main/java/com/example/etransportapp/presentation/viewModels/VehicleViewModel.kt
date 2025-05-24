package com.example.etransportapp.presentation.viewModels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.Vehicle
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.model.vehicle.FetchUserVehiclesResponse
import com.example.etransportapp.data.model.vehicle.VehicleRequest
import com.example.etransportapp.data.model.vehicle.VehicleResponse
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleViewModel : ViewModel() {

    private val _myVehicles2 = MutableStateFlow<List<Vehicle>>(emptyList())
    val myVehicles2: StateFlow<List<Vehicle>> = _myVehicles2

    private val _myVehicles = MutableStateFlow<List<FetchUserVehiclesResponse>>(emptyList())
    val myVehicles: StateFlow<List<FetchUserVehiclesResponse>> = _myVehicles

    private val _selectedVehicleById = MutableStateFlow<FetchUserVehiclesResponse?>(null)
    val selectedVehicleById: StateFlow<FetchUserVehiclesResponse?> = _selectedVehicleById

    var adOwnerInfo = mutableStateOf<UserProfileResponse?>(null)
        private set


    fun fetchAdOwner(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUserProfile(userId)
                if (response.isSuccessful) {
                    adOwnerInfo.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun addVehicle(context: Context, vehicle: VehicleRequest, onComplete: () -> Unit) {
        val userId = PreferenceHelper.getUserId(context) ?: return

        val request = vehicle.copy(carrierId = userId)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleApi.addVehicle(request)
                if (response.isSuccessful) {
                    val addedVehicle = response.body()
                    if (addedVehicle != null) {
                        // 🔽 BU KISMI EKLE
                        val converted = FetchUserVehiclesResponse(
                            id = addedVehicle.id,
                            licensePlate = addedVehicle.licensePlate,
                            capacity = addedVehicle.capacity,
                            model = addedVehicle.model,
                            title = addedVehicle.title,
                            carrierId = addedVehicle.userId,
                            carrierName = "", // API'den gelmiyor
                            vehicleType = addedVehicle.vehicleType
                        )
                        _myVehicles.value = _myVehicles.value + converted
                        // 🔼

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Araç başarıyla eklendi", Toast.LENGTH_SHORT).show()
                            onComplete()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Araç eklenemedi", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun fetchVehicleById(vehicleId: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleApi.getVehicleById(vehicleId)
                if (response.isSuccessful) {
                    _selectedVehicleById.value = response.body()
                } else {
                    _selectedVehicleById.value = null
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Araç bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                _selectedVehicleById.value = null
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Araç bilgisi alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun fetchVehiclesByUser(context: Context) {
        val userId = PreferenceHelper.getUserId(context) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleApi.getVehiclesByUser(userId)
                if (response.isSuccessful) {
                    val list = response.body()
                    if (list != null) {
                        _myVehicles.value = list
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Araçlar alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun deleteVehicle(context: Context, vehicle: VehicleResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleApi.deleteVehicle(vehicle.id)
                if (response.isSuccessful) {
                    _myVehicles.value = _myVehicles.value.filterNot { it.id == vehicle.id }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Araç silindi", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Silme başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateVehicle(context: Context, id: Int, request: VehicleRequest, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleApi.updateVehicle(id, request)
                if (response.isSuccessful) {
                    val updatedList = _myVehicles.value.map {
                        if (it.id == id) it.copy(
                            title = request.title,
                            vehicleType = request.vehicleType,
                            capacity = request.capacity,
                            licensePlate = request.licensePlate,
                            model = request.model
                        ) else it
                    }
                    _myVehicles.value = updatedList
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Araç güncellendi", Toast.LENGTH_SHORT).show()
                        onComplete()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Güncelleme başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
