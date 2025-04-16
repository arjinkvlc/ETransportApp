package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.LoadAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoadAdViewModel : ViewModel() {
    private val _loadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val loadAds: StateFlow<List<LoadAd>> = _loadAds

    private val _myLoadAds = MutableStateFlow<List<LoadAd>>(emptyList())
    val myLoadAds: StateFlow<List<LoadAd>> = _myLoadAds

    fun addLoadAd(ad: LoadAd) {
        _loadAds.value = _loadAds.value + ad
        // Örnek: "username" sabit kullanıcı id
        if (ad.userId == "username") {
            _myLoadAds.value = _myLoadAds.value + ad
        }
    }
}
