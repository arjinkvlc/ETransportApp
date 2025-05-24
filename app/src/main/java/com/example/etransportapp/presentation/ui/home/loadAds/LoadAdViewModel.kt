package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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


    var selectedSort by mutableStateOf("Tümü")
    var selectedFilter by mutableStateOf("Tümü")

    val sortedLoadAds = derivedStateOf {
        when (selectedSort) {
            "En Yeni" -> loadAds.value.sortedBy {it.date }
            "Ucuzdan Pahalı" -> loadAds.value.sortedBy { it.price }
            "Pahalıdan Ucuza" -> loadAds.value.sortedByDescending { it.price }
            else -> loadAds.value
        }
    }

    val filteredLoadAds = derivedStateOf {
        when (selectedFilter) {
            "Yurtiçi" -> loadAds.value.filter { /* örnek filtreleme */ true }
            "Uluslararası" -> loadAds.value.filter { /* örnek filtreleme */ true }
            else -> loadAds.value
        }
    }

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
}
