package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.remote.api.GeoPlace
import com.example.etransportapp.data.remote.service.CurrencyService
import com.example.etransportapp.data.remote.service.HereService
import com.example.etransportapp.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadAdViewModel : ViewModel() {
    private val _loadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val loadAds: StateFlow<List<LoadAd>> = _loadAds
    var selectedAd: LoadAd? = null

    private val _myLoadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val myLoadAds: StateFlow<List<LoadAd>> = _myLoadAds

    private val hereApi = HereService.create()
    val currencyApi = CurrencyService.create()

    private val _suggestedCostText = MutableStateFlow<String?>(null)
    val suggestedCostText: StateFlow<String?> = _suggestedCostText

    fun addLoadAd(ad: LoadAd) {
        _loadAds.value = _loadAds.value + ad
        if (ad.userId == "username") {
            _myLoadAds.value = _myLoadAds.value + ad
        }
    }

    fun deleteAd(ad: LoadAd) {
        _loadAds.value = _loadAds.value.filterNot { it == ad }
        _myLoadAds.value = _myLoadAds.value.filterNot { it == ad }
    }

    fun updateAd(updatedAd: LoadAd) {
        _loadAds.value = _loadAds.value.map { if (it == selectedAd) updatedAd else it }
        _myLoadAds.value = _myLoadAds.value.map { if (it == selectedAd) updatedAd else it }
        selectedAd = updatedAd
    }

    fun calculateRouteAndPredictCost(
        origin: GeoPlace,
        destination: GeoPlace,
        weightKg: Int,
        cargoType: String
    ) {
        viewModelScope.launch {
            try {
                val response = hereApi.calculateRoute(
                    apiKey = Constants.HERE_API_KEY,
                    waypoint0 = "${origin.lat},${origin.lng}",
                    waypoint1 = "${destination.lat},${destination.lng}",
                    weight = weightKg
                )


                val route = response.response.route.firstOrNull()
                val distanceKm = route?.summary?.distance?.div(1000.0) ?: 0.0
                val tollCost = route?.cost?.details?.tollCost?.toDoubleOrNull() ?: 0.0
                val tollCurrency = route?.cost?.currency ?: "EUR"

                convertToTry(tollCost, tollCurrency) { convertedToll ->
                    _suggestedCostText.value = """
                 HERE API Çıktısı:
                • Mesafe: %.1f km
                • Geçiş Ücreti: ₺%.2f
                """.trimIndent().format(distanceKm, convertedToll)

                    println("HERE Mesafe: $distanceKm km, Geçiş Ücreti: ₺$convertedToll (orijinal $tollCost $tollCurrency)")
                }

            } catch (e: Exception) {
                _suggestedCostText.value = "Tahmin alınamadı: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    private fun convertToTry(amount: Double, fromCurrency: String, onResult: (Double) -> Unit) {
        viewModelScope.launch {
            try {
                val response = currencyApi.getRates(fromCurrency)
                val tryRate = response.rates["TRY"] ?: 1.0
                val converted = amount * tryRate
                onResult(converted)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(amount)
            }
        }
    }
}
