package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.presentation.components.InfoText
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.presentation.components.CountryCitySelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdDetailScreen(
    vehicleAd: VehicleAdGetResponse,
    navController: NavHostController,
    isMyAd: Boolean = vehicleAd.carrierId == "username",
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((VehicleAdGetResponse) -> Unit)? = null,
    vehicleViewModel: VehicleViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(vehicleAd.title) }
    var description by remember { mutableStateOf(vehicleAd.description) }
    var selectedCountry by remember { mutableStateOf(vehicleAd.country) }
    var selectedCity by remember { mutableStateOf(vehicleAd.city) }
    var capacity by remember { mutableStateOf(vehicleAd.capacity.toString()) }
    var selectedCargoType by remember { mutableStateOf(vehicleAd.vehicleType.ifBlank { "Açık Kasa" }) }
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }

    val geoNamesViewModel: GeoNamesViewModel = viewModel()

    val cargoTypes = listOf("Açık Kasa", "Tenteli", "Frigofirik", "Tanker", "Diğer")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Araç İlan Detayı", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (isMyAd) {
                        if (isEditing) {
                            IconButton(onClick = {
                                onUpdateClick?.invoke(
                                    vehicleAd.copy(
                                        title = title,
                                        description = description,
                                        vehicleType = selectedCargoType,
                                        country = selectedCountry,
                                        city = selectedCity,
                                        capacity = capacity.toIntOrNull() ?: 0
                                    )
                                )
                                isEditing = false
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Kaydet", tint = Color.White)
                            }
                        } else {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Düzenle", tint = Color.White)
                            }
                        }
                        IconButton(onClick = { onDeleteClick?.invoke() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = DarkGray)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Taşıma Kapasitesi (ton)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                ExposedDropdownMenuBox(
                    expanded = isCargoTypeMenuExpanded,
                    onExpandedChange = { isCargoTypeMenuExpanded = !isCargoTypeMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCargoType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Yük Tipi") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCargoTypeMenuExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCargoTypeMenuExpanded,
                        onDismissRequest = { isCargoTypeMenuExpanded = false }
                    ) {
                        cargoTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    selectedCargoType = type
                                    isCargoTypeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                CountryCitySelector(
                    username = Constants.GEO_NAMES_USERNAME,
                    geoViewModel = geoNamesViewModel,
                    onSelected = { countryCode, cityName ->
                        selectedCountry = geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                        selectedCity = cityName
                    }
                )
            } else {
                InfoText("Başlık", title)
                InfoText("Açıklama", description)
                InfoText("Taşıma Kapasitesi", "$capacity ton")
                InfoText("Konum", "$selectedCity, $selectedCountry")
                InfoText("Tarih", vehicleAd.createdDate.substring(0, 10))
            }
        }
    }
}
