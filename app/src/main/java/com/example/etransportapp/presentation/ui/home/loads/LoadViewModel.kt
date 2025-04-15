package com.example.etransportapp.presentation.ui.home.loads

import androidx.lifecycle.ViewModel
import com.example.etransportapp.data.model.LoadItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoadViewModel : ViewModel() {
    private val _loads = MutableStateFlow<List<LoadItem>>(emptyList())
    val loads: StateFlow<List<LoadItem>> = _loads

    fun addLoad(item: LoadItem) {
        _loads.value = _loads.value + item
    }
}
