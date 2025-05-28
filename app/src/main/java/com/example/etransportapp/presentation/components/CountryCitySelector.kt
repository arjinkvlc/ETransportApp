package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.etransportapp.data.remote.api.Country
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCitySelector(
    username: String,
    geoViewModel: GeoNamesViewModel,
    onSelected: (countryCode: String, cityName: String) -> Unit
) {
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var selectedCity by remember { mutableStateOf("") }
    var countryQuery by remember { mutableStateOf("") }
    var cityQuery by remember { mutableStateOf("") }

    val countries by geoViewModel.countries.collectAsState()
    val cities by geoViewModel.cities.collectAsState()

    var showCountryDropdown by remember { mutableStateOf(false) }
    var showCityDropdown by remember { mutableStateOf(false) }

    val countryFocusRequester = remember { FocusRequester() }
    val cityFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        geoViewModel.fetchCountries(username)
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // ÜLKE
        ExposedDropdownMenuBox(
            expanded = showCountryDropdown,
            onExpandedChange = { showCountryDropdown = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = countryQuery,
                onValueChange = {
                    countryQuery = it
                    showCountryDropdown = true
                },
                label = { Text("Ülke") },
                modifier = Modifier
                    .menuAnchor()
                    .focusRequester(countryFocusRequester),
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCountryDropdown) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(focusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
            )

            ExposedDropdownMenu(
                expanded = showCountryDropdown,
                onDismissRequest = { showCountryDropdown = false }
            ) {
                countries.filter {
                    it.countryName.contains(countryQuery, ignoreCase = true)
                }.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country.countryName) },
                        onClick = {
                            selectedCountry = country
                            countryQuery = country.countryName
                            showCountryDropdown = false
                            cityQuery = ""
                            selectedCity = ""
                            geoViewModel.searchCities(query = "", countryCode = country.countryCode, username = username)

                        }
                    )
                }
            }
        }

        // ŞEHİR
        ExposedDropdownMenuBox(
            expanded = showCityDropdown,
            onExpandedChange = { showCityDropdown = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = cityQuery,
                onValueChange = {
                    cityQuery = it
                    showCityDropdown = true
                    selectedCountry?.let { country ->
                        geoViewModel.searchCities(query = cityQuery, countryCode = country.countryCode, username = username)
                    }
                },
                label = { Text("Şehir") },
                modifier = Modifier
                    .menuAnchor()
                    .focusRequester(cityFocusRequester),
                singleLine = true,
                enabled = selectedCountry != null,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCityDropdown) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(focusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
            )

            ExposedDropdownMenu(
                expanded = showCityDropdown,
                onDismissRequest = { showCityDropdown = false }
            ) {
                cities.filter {
                    it.name.contains(cityQuery, ignoreCase = true)
                }.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city.name) },
                        onClick = {
                            cityQuery = city.name
                            selectedCity = city.name
                            showCityDropdown = false
                            selectedCountry?.let {
                                onSelected(it.countryCode, selectedCity)
                            }
                        }
                    )
                }
            }
        }
    }
}
