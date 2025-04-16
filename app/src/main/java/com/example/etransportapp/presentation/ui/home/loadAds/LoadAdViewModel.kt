package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.LoadAdItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoadAdViewModel : ViewModel() {
    private val _loads = MutableStateFlow<List<LoadAdItem>>(emptyList())
    val loads: StateFlow<List<LoadAdItem>> = _loads

    fun addLoad(item: LoadAdItem) {
        _loads.value = _loads.value + item
    }
}
