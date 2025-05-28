package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.ad.CargoAdCreateRequest
import com.example.etransportapp.data.model.ad.CargoAdPriceSuggestionRequest
import com.example.etransportapp.data.model.ad.CargoAdResponse
import com.example.etransportapp.data.model.ad.CargoAdUpdateRequest
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.model.offer.CargoOfferRequest
import com.example.etransportapp.data.model.offer.CargoOfferResponse
import com.example.etransportapp.data.model.offer.CargoOfferStatusUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoadAdViewModel : ViewModel() {
    val sentOffers = mutableStateOf<List<CargoOfferResponse>>(emptyList())

    private val _cargoAdOffers = mutableStateOf<List<CargoOfferResponse>>(emptyList())
    val cargoAdOffers: State<List<CargoOfferResponse>> = _cargoAdOffers
    val senderInfoMap = mutableStateMapOf<String, UserProfileResponse>()

    private val _loadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val loadAds: StateFlow<List<CargoAdResponse>> = _loadAds

    private val _filteredLoadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val filteredLoadAds: StateFlow<List<CargoAdResponse>> = _filteredLoadAds

    var selectedAd: CargoAdResponse? = null

    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")

    private val _suggestedPriceText = mutableStateOf<String?>(null)
    val suggestedPriceText: State<String?> = _suggestedPriceText


    init {
        combine(
            loadAds,
            snapshotFlow { selectedSort },
            snapshotFlow { selectedFilter }
        ) { ads, sort, filter ->
            val sorted = when (sort) {
                "En Yeni" -> ads.sortedBy { it.createdDate }
                "En Eski" -> ads.sortedByDescending { it.createdDate }
                "Ucuzdan Pahalıya" -> ads.sortedBy { it.price }
                "Pahalıdan Ucuza" -> ads.sortedByDescending { it.price }
                else -> ads
            }

            if (filter == "Tümü") sorted
            else sorted.filter { it.cargoType == filter }
        }.onEach {
            _filteredLoadAds.value = it
        }.launchIn(viewModelScope)
    }


    fun fetchAllCargoAds() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoAdApi.getAllCargoAds()
                if (response.isSuccessful) {
                    _loadAds.value = response.body() ?: emptyList()
                    println("Yüklenen kargo ilanları: ${_loadAds.value}")
                } else {
                    println("Kargo ilanları çekilemedi: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Hata oluştu: ${e.message}")
            }
        }
    }

    var adOwnerInfo = mutableStateOf<UserProfileResponse?>(null)
        private set

    fun fetchAdOwnerInfo(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUserProfile(userId)
                if (response.isSuccessful) {
                    adOwnerInfo.value = response.body()
                } else {
                    println("Kullanıcı bilgisi alınamadı: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Hata oluştu: ${e.message}")
            }
        }
    }

    fun updateCargoAd(ad: CargoAdUpdateRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoAdApi.updateCargoAd(ad.id, ad)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Güncelleme başarısız: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Sunucu hatası: ${e.message}")
            }
        }
    }

    fun deleteCargoAd(id: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoAdApi.deleteCargoAd(id)
                if (response.isSuccessful) {
                    fetchAllCargoAds()
                    onSuccess()
                } else {
                    onError("Silme başarısız: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun createCargoAd(
        request: CargoAdCreateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoAdApi.createCargoAd(request)
                if (response.isSuccessful) {
                    fetchAllCargoAds()
                    onSuccess()
                } else {
                    onError("İlan oluşturulamadı: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Sunucu hatası: ${e.localizedMessage}")
            }
        }
    }

    fun createCargoOffer(
        request: CargoOfferRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoOfferApi.createCargoOffer(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Teklif gönderilemedi: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun fetchSentOffers(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoOfferApi.getOffersBySender(userId)
                if (response.isSuccessful) {
                    sentOffers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                println("Teklifler alınamadı: ${e.message}")
            }
        }
    }

    fun fetchOffersByCargoAdId(cargoAdId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.cargoOfferApi.getOffersByCargoAd(cargoAdId)
                if (response.isSuccessful) {
                    _cargoAdOffers.value = response.body() ?: emptyList()
                } else {
                    println("Teklifler alınamadı: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Teklifleri çekerken hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun fetchUserInfo(userId: String) {
        viewModelScope.launch {
            if (senderInfoMap.contains(userId)) return@launch
            try {
                val response = RetrofitInstance.userApi.getUserProfile(userId)
                if (response.isSuccessful) {
                    response.body()?.let { senderInfoMap[userId] = it }
                }
            } catch (e: Exception) {
                println("Kullanıcı bilgisi alınamadı: ${e.localizedMessage}")
            }
        }
    }

    fun cancelOffer(offerId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = CargoOfferStatusUpdateRequest(offerId = offerId, status = "Cancelled")
                val response = RetrofitInstance.cargoOfferApi.updateOfferStatus(offerId, request)
                if (response.isSuccessful) {
                    val updatedOffers = _cargoAdOffers.value.map {
                        if (it.id == offerId) it.copy(status = "Cancelled") else it
                    }
                    _cargoAdOffers.value = updatedOffers

                    onSuccess()
                } else {
                    onError("Teklif durumu güncellenemedi: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun fetchSuggestedPrice(
        pickCountry: String,
        pickCity: String,
        dropCountry: String,
        dropCity: String,
        weight: Double,
        cargoType: String
    ) {
        viewModelScope.launch {
            try {
                val request = CargoAdPriceSuggestionRequest(
                    pickCountry = pickCountry,
                    pickCity = pickCity,
                    dropCountry = dropCountry,
                    dropCity = dropCity,
                    weight = weight,
                    cargoType = cargoType
                )
                val response = RetrofitInstance.cargoAdApi.predictSuggestedPrice(request)
                if (response.isSuccessful) {
                    val range = response.body()
                    _suggestedPriceText.value = range?.takeIf { it.isNotEmpty() }
                        ?.let { "Önerilen Fiyat Aralığı: ${it.first()} - ${it.last()}" }
                }
            } catch (e: Exception) {
                _suggestedPriceText.value = "Sunucu hatası: ${e.message}"
            }
        }
    }

}
