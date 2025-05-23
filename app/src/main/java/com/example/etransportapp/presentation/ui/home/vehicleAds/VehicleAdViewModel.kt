package com.example.etransportapp.presentation.ui.home.vehicleAds

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.ad.VehicleAdCreateRequest
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.data.model.ad.VehicleAdUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleAdViewModel : ViewModel() {

    private val _vehicleAds = MutableStateFlow<List<VehicleAdGetResponse>>(emptyList())
    val vehicleAds: StateFlow<List<VehicleAdGetResponse>> = _vehicleAds

    var selectedAd: VehicleAdGetResponse? = null
    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")

    val sortedLoadAds = derivedStateOf {
        when (selectedSort) {
            "En Yeni" -> vehicleAds.value.sortedBy { it.createdDate }
            "En Eski" -> vehicleAds.value.sortedByDescending { it.createdDate }
            "Taşıma Kapasitesi" -> vehicleAds.value.sortedBy { it.capacity }
            else -> vehicleAds.value
        }
    }

    val filteredLoadAds = derivedStateOf {
        when (selectedFilter) {
            "Açık Kasa" -> sortedLoadAds.value.filter { it.vehicleType == "Açık Kasa" }
            "Tenteli" -> sortedLoadAds.value.filter { it.vehicleType == "Tenteli" }
            "Frigofirik" -> sortedLoadAds.value.filter { it.vehicleType == "Frigofirik" }
            "Tanker" -> sortedLoadAds.value.filter { it.vehicleType == "Tanker" }
            else -> sortedLoadAds.value
        }
    }

    fun fetchAllVehicleAds() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.vehicleAdApi.getAllVehicleAds()
                if (response.isSuccessful) {
                    _vehicleAds.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createVehicleAd(
        context: Context,
        request: VehicleAdCreateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.vehicleAdApi.createVehicleAd(request)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "İlan oluşturuldu", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("Hata: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Sunucu hatası: ${e.message}")
                }
            }
        }
    }

    fun deleteAd(context: Context, ad: VehicleAdGetResponse, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.vehicleAdApi.deleteVehicleAd(ad.id)
                if (response.isSuccessful) {
                    _vehicleAds.value = _vehicleAds.value.filterNot { it.id == ad.id }
                    Toast.makeText(context, "İlan silindi", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Silme başarısız", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateAd(context: Context, updatedAd: VehicleAdGetResponse, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val request = VehicleAdUpdateRequest(
                id = updatedAd.id,
                title = updatedAd.title,
                description = updatedAd.description,
                country = updatedAd.country,
                city = updatedAd.city,
                vehicleType = updatedAd.vehicleType,
                capacity = updatedAd.capacity
            )

            try {
                val response = RetrofitInstance.vehicleAdApi.updateVehicleAd(updatedAd.id, request)
                if (response.isSuccessful) {
                    _vehicleAds.value = _vehicleAds.value.map {
                        if (it.id == updatedAd.id) updatedAd else it
                    }
                    Toast.makeText(context, "İlan güncellendi", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Güncelleme başarısız", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
