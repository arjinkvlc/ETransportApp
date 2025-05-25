package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.ad.CargoAdCreateRequest
import com.example.etransportapp.data.model.ad.CargoAdResponse
import com.example.etransportapp.data.model.ad.CargoAdUpdateRequest
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.data.model.auth.UserProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadAdViewModel : ViewModel() {
    private val _loadAds = MutableStateFlow<List<CargoAdResponse>>(emptyList())
    val loadAds: StateFlow<List<CargoAdResponse>> = _loadAds
    var selectedAd: CargoAdResponse? = null
    var selectedAd2: LoadAd? = null

    private val _myLoadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val myLoadAds: StateFlow<List<LoadAd>> = _myLoadAds


    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")
/*
    val sortedLoadAds = derivedStateOf {
        when (selectedSort) {
            "En Yeni" -> loadAds.value.sortedBy {it.date }
            "Ucuzdan Pahalı" -> loadAds.value.sortedBy { it.price }
            "Pahalıdan Ucuza" -> loadAds.value.sortedByDescending { it.price }
            else -> loadAds.value
        }
    }*/

    val filteredLoadAds = derivedStateOf {
        when (selectedFilter) {
            "Yurtiçi" -> loadAds.value.filter { /* örnek filtreleme */ true }
            "Uluslararası" -> loadAds.value.filter { /* örnek filtreleme */ true }
            else -> loadAds.value
        }
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



    /*
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
    }*/
}
