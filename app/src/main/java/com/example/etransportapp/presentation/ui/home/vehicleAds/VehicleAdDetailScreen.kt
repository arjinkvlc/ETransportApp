package com.example.etransportapp.presentation.ui.home.vehicleAds

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.VehicleAdGetResponse
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.presentation.components.AdDetailTabRow
import com.example.etransportapp.presentation.components.AdOwnerInfoSection
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.presentation.components.VehicleAdDetailSection
import com.example.etransportapp.presentation.components.VehicleOfferDialog
import com.example.etransportapp.util.PreferenceHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdDetailScreen(
    vehicleAd: VehicleAdGetResponse,
    navController: NavHostController,
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((VehicleAdGetResponse) -> Unit)? = null,
    vehicleViewModel: VehicleViewModel
) {
    val context = LocalContext.current
    val currentUserId = remember { PreferenceHelper.getUserId(context) }

    val isMyAd = remember { vehicleAd.carrierId == currentUserId }

    val focusManager = LocalFocusManager.current

    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(vehicleAd.title) }
    var description by remember { mutableStateOf(vehicleAd.description) }
    var selectedCountry by remember { mutableStateOf(vehicleAd.country) }
    var selectedCity by remember { mutableStateOf(vehicleAd.city) }
    var capacity by remember { mutableStateOf(vehicleAd.capacity.toString()) }
    var selectedCargoType by remember { mutableStateOf(vehicleAd.vehicleType.ifBlank { "Açık Kasa" }) }
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }
    var showVehicleOfferDialog by remember { mutableStateOf(false) }
    var offerMessage by remember { mutableStateOf("") }

    val geoNamesViewModel: GeoNamesViewModel = viewModel()
    val cargoTypes = listOf("Açık Kasa", "Tenteli", "Frigofirik", "Tanker", "Diğer")
    val tabs = listOf("İlan Detayı", "İlan Sahibi")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Araç İlan Detayı", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
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
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
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
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCargoTypeMenuExpanded) },
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

                Text(text = "Konum", color = DarkGray, style = MaterialTheme.typography.titleSmall)
                CountryCitySelector(
                    username = Constants.GEO_NAMES_USERNAME,
                    geoViewModel = geoNamesViewModel,
                    onSelected = { countryCode, cityName ->
                        selectedCountry = geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                        selectedCity = cityName
                    }
                )
            } else {
                AdDetailTabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )

                when (selectedTabIndex) {
                    0 -> {
                        VehicleAdDetailSection(
                            title = title,
                            description = description,
                            cargoType = vehicleAd.vehicleType,
                            capacity = capacity,
                            location = vehicleAd.city,
                            date = vehicleAd.createdDate
                        )
                    }

                    1 -> {
                        // TODO: Replace with actual user data
                        AdOwnerInfoSection(
                            name = "Mehmet Yılmaz",
                            email = "mehmet@example.com",
                            phone = "+90 555 123 45 67"
                        )
                    }
                }


                Spacer(Modifier.weight(1f))


                if (isMyAd) {
                    Button(
                        onClick = {
                            navController.navigate("vehicleAdOffers/${vehicleAd.id}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Gelen Teklifleri Gör")
                    }
                } else {
                    Button(
                        onClick = { showVehicleOfferDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Teklif Gönder")
                    }
                }

                if (showVehicleOfferDialog) {
                    VehicleOfferDialog(
                        message = offerMessage,
                        onMessageChange = { offerMessage = it },
                        onDismiss = {
                            showVehicleOfferDialog = false
                            offerMessage = ""
                        },
                        onConfirm = {
                            // TODO: Teklifi backend'e gönder
                            showVehicleOfferDialog = false
                            offerMessage = ""
                        }
                    )
                }

            }
        }
    }
}

