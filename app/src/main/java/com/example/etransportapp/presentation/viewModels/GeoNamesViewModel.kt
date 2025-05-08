package com.example.etransportapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.remote.api.Country
import com.example.etransportapp.data.remote.api.GeoPlace
import com.example.etransportapp.data.remote.service.GeoNamesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeoNamesViewModel : ViewModel() {
    private val api = GeoNamesService.create()

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _cities = MutableStateFlow<List<GeoPlace>>(emptyList())
    val cities: StateFlow<List<GeoPlace>> = _cities

    fun fetchCountries(username: String) {
        viewModelScope.launch {
            try {
                val result = api.getAllCountries(username)
                _countries.value = result.geonames
            } catch (e: Exception) {
                _countries.value = emptyList()
            }
        }
    }

    fun searchCities(query: String, countryCode: String, username: String) {
        viewModelScope.launch {
            try {
                val result = api.searchCitiesByCountry(
                    query = query,
                    countryCode = countryCode,
                    username = username
                )
                _cities.value = result.geonames
            } catch (e: Exception) {
                _cities.value = emptyList()
            }
        }
    }
}
