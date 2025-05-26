package com.example.etransportapp.presentation.ui.home.vehicleAds

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.ad.VehicleAdCreateRequest
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.data.model.ad.VehicleAdUpdateRequest
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.model.offer.VehicleOfferRequest
import com.example.etransportapp.data.model.offer.VehicleOfferResponse
import com.example.etransportapp.data.model.offer.VehicleOfferStatusUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleAdViewModel : ViewModel() {

    private val _vehicleAds = MutableStateFlow<List<VehicleAdGetResponse>>(emptyList())
    val vehicleAds: StateFlow<List<VehicleAdGetResponse>> = _vehicleAds

    var selectedAd: VehicleAdGetResponse? = null
    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")

    var offerSentMap = mutableStateMapOf<Int, Boolean>()

    val sentVehicleOffers = mutableStateOf<List<VehicleOfferResponse>>(emptyList())

    val vehicleAdOffers = mutableStateOf<List<VehicleOfferResponse>>(emptyList())
    val senderInfoMap = mutableStateMapOf<String, UserProfileResponse>()

    private val _filteredVehicleAds = MutableStateFlow<List<VehicleAdGetResponse>>(emptyList())
    val filteredVehicleAds: StateFlow<List<VehicleAdGetResponse>> = _filteredVehicleAds

    init {
        combine(vehicleAds, snapshotFlow { selectedFilter }, snapshotFlow { selectedSort }) { ads, filter, sort ->
            val sorted = when (sort) {
                "En Yeni" -> ads.sortedBy { it.createdDate }
                "En Eski" -> ads.sortedByDescending { it.createdDate }
                "Taşıma Kapasitesi" -> ads.sortedBy { it.capacity }
                else -> ads
            }

            if (filter == "Tümü") sorted
            else sorted.filter { it.vehicleType == filter }
        }.onEach {
            _filteredVehicleAds.value = it
        }.launchIn(viewModelScope)
    }



    val sortedVehicleAds = derivedStateOf {
        when (selectedSort) {
            "En Yeni" -> vehicleAds.value.sortedBy { it.createdDate }
            "En Eski" -> vehicleAds.value.sortedByDescending { it.createdDate }
            "Taşıma Kapasitesi" -> vehicleAds.value.sortedBy { it.capacity }
            else -> vehicleAds.value
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

    fun createVehicleOffer(
        request: VehicleOfferRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.vehicleOfferApi.createVehicleOffer(request)
                if (response.isSuccessful) {
                    offerSentMap[request.vehicleAdId] = true
                    onSuccess()
                } else {
                    onError("Teklif gönderilemedi: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun fetchSentVehicleOffers(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.vehicleOfferApi.getVehicleOffersBySenderId(userId)
                if (response.isSuccessful) {
                    sentVehicleOffers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchVehicleOffersByVehicleAdId(adId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.vehicleOfferApi.getVehicleOffersByVehicleAdId(adId)
                if (response.isSuccessful) {
                    vehicleAdOffers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchUserInfo(userId: String) {
        if (senderInfoMap.containsKey(userId)) return

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUserProfile(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        senderInfoMap[userId] = it
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelVehicleOffer(offerId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = VehicleOfferStatusUpdateRequest(
                    offerId = offerId,
                    status = "Cancelled"
                )
                val response = RetrofitInstance.vehicleOfferApi.updateVehicleOfferStatus(offerId, request)
                if (response.isSuccessful) {
                    vehicleAdOffers.value = vehicleAdOffers.value.map {
                        if (it.id == offerId) it.copy(status = "Cancelled") else it
                    }
                    onSuccess()
                } else {
                    onError("İşlem başarısız")
                }
            } catch (e: Exception) {
                onError("Sunucu hatası: ${e.message}")
            }
        }
    }

}
