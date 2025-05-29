package com.example.etransportapp.presentation.ui.home.loadAds

import RetrofitInstance
import android.content.Context
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
import com.example.etransportapp.data.remote.service.CurrencyService
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class LoadAdViewModel : ViewModel() {
    val sentOffers = mutableStateOf<List<CargoOfferResponse>>(emptyList())

    private val _cargoAdOffers = mutableStateOf<List<CargoOfferResponse>>(emptyList())
    val cargoAdOffers: State<List<CargoOfferResponse>> = _cargoAdOffers
    val senderInfoMap = mutableStateMapOf<String, UserProfileResponse>()

    private val _loadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val loadAds: StateFlow<List<CargoAdResponse>> = _loadAds

    private val _myLoadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val myLoadAds: StateFlow<List<CargoAdResponse>> = _myLoadAds

    private val _filteredLoadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val filteredLoadAds: StateFlow<List<CargoAdResponse>> = _filteredLoadAds

    var selectedAd: CargoAdResponse? = null

    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")

    private val _suggestedPriceText = mutableStateOf<String?>(null)
    val suggestedPriceText: State<String?> = _suggestedPriceText

    private val _exchangeRates = mutableStateOf<Map<String, Double>>(emptyMap())
    val exchangeRates: State<Map<String, Double>> = _exchangeRates


    init {
        combine(
            loadAds,
            snapshotFlow { selectedSort },
            snapshotFlow { selectedFilter }
        ) { ads, sort, filter ->
            val sorted = when (sort) {
                "En Yeni" -> ads.sortedBy { it.createdDate }
                "En Eski" -> ads.sortedByDescending { it.createdDate }
                "Ucuzdan Pahalıya" -> ads.sortedByDescending { it.price }
                "Pahalıdan Ucuza" -> ads.sortedBy { it.price }
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

    fun fetchMyCargoAds(context: Context) {
        viewModelScope.launch {
            try {
                val response = PreferenceHelper.getUserId(context)
                    ?.let { RetrofitInstance.cargoAdApi.getCargoAdsByCustomer(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _myLoadAds.value = response.body() ?: emptyList()
                        println("Yüklenen kargo ilanları: ${_loadAds.value}")
                    } else {
                        println("Kargo ilanları çekilemedi: ${response.code()}")
                    }
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
                    cargoType = cargoType,
                    pickCity = pickCity,
                    pickCountry = pickCountry,
                    deliveryCity = dropCity,
                    deliveryCountry = dropCountry,
                    weight = weight
                )

                val response = RetrofitInstance.cargoAdApi.predictSuggestedPrice(request)

                if (response.isSuccessful) {
                    response.body()?.let { priceResponse ->
                        val min = roundToNearestFifty(priceResponse.price.minPrice)
                        val max = roundToNearestFifty(priceResponse.price.maxPrice)
                        val distance = priceResponse.distance.roundToInt()

                        _suggestedPriceText.value =
                            if (min == max)
                                "$min EUR \n Mesafe: $distance km"
                            else
                                "$min - $max EUR \n Mesafe: $distance km"
                    } ?: run {
                        _suggestedPriceText.value = "Sunucudan veri alınamadı."
                    }
                } else {
                    _suggestedPriceText.value = "Hata: ${response.code()}"
                }
            } catch (e: Exception) {
                _suggestedPriceText.value = "Sunucu hatası: ${e.message}"
            }
        }
    }

    private fun roundToNearestFifty(value: Double): Int {
        return (Math.round(value / 50.0) * 50).toInt()
    }


    fun fetchExchangeRates(base: String = "EUR") {
        viewModelScope.launch {
            try {
                val response = CurrencyService.create().getRates(base)
                if (response.result == "success") {
                    _exchangeRates.value = response.rates
                } else {
                    println("Kur verisi alınamadı: ${response.result}")
                }
            } catch (e: Exception) {
                println("Kur verisi hatası: ${e.localizedMessage}")
            }
        }
    }

    fun getConvertedSuggestedPrice(targetCurrency: String): String? {
        val eurText = _suggestedPriceText.value ?: return null
        val rates = _exchangeRates.value
        val rate = rates[targetCurrency] ?: return eurText

        val regex = Regex("""(\d+)(?: - (\d+))? EUR\s+\n\s+Mesafe: (\d+) km""")
        val match = regex.find(eurText) ?: return eurText

        val minPriceEur = match.groupValues[1].toDoubleOrNull() ?: return eurText
        val maxPriceEur = match.groupValues.getOrNull(2)?.toDoubleOrNull()
        val distanceKm = match.groupValues[3]

        return if (maxPriceEur != null && minPriceEur != maxPriceEur) {
            val convertedMin = roundToNearestFifty(minPriceEur * rate)
            val convertedMax = roundToNearestFifty(maxPriceEur * rate)
            "$convertedMin - $convertedMax $targetCurrency \n Mesafe: $distanceKm km"
        } else {
            val converted = roundToNearestFifty(minPriceEur * rate)
            "$converted $targetCurrency \n Mesafe: $distanceKm km"
        }
    }
}

fun convertStatusToString(status: String): String {
    return when (status) {
        "Accepted"-> "Aktif"
        "Pending" -> "Beklemede"
        "Rejected" -> "İptal Edildi"
        else -> "Bilinmiyor"
    }
}