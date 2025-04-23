package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.ad.LoadAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoadAdViewModel : ViewModel() {
    private val _loadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val loadAds: StateFlow<List<LoadAd>> = _loadAds
    var selectedAd: LoadAd? = null

    private val _myLoadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val myLoadAds: StateFlow<List<LoadAd>> = _myLoadAds

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
