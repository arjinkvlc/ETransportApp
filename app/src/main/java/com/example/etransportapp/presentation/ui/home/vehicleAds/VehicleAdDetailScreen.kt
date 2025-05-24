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
import com.example.etransportapp.data.model.ad.VehicleAd
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.presentation.components.AdDetailTabRow
import com.example.etransportapp.presentation.components.AdOwnerInfoSection
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.presentation.components.VehicleAdDetailSection
import com.example.etransportapp.presentation.components.VehicleOfferDialog
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdDetailScreen(
    vehicleAd: VehicleAd,
    navController: NavHostController,
    isMyAd: Boolean = vehicleAd.userId == "username",
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((VehicleAd) -> Unit)? = null,
    vehicleViewModel: VehicleViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vehicleViewModel.fetchVehiclesByUser(context)
    }

    var isEditing by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    var title by remember { mutableStateOf(vehicleAd.title) }
    var description by remember { mutableStateOf(vehicleAd.description) }
    var location by remember { mutableStateOf(vehicleAd.location) }
    var date by remember { mutableStateOf(vehicleAd.date) }
    var capacity by remember { mutableStateOf(vehicleAd.capacity) }
    val cargoType = vehicleAd.cargoType.ifBlank { "Açık Kasa" }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val geoNamesViewModel: GeoNamesViewModel = viewModel()

    var selectedCargoType by remember { mutableStateOf(vehicleAd.cargoType.ifBlank { "Açık Kasa" }) }
    val cargoTypes = listOf("Açık Kasa", "Tenteli", "Frigofirik", "Tanker", "Diğer")
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }
    var showVehicleOfferDialog by remember { mutableStateOf(false) }
    var offerMessage by remember { mutableStateOf("") }
    var showVehiclePicker by remember { mutableStateOf(false) }

    val selectedVehicle by vehicleViewModel.selectedVehicleById.collectAsState()
    val tabs = listOf("İlan Detayı", "İlan Sahibi")
    var selectedTabIndex by remember { mutableStateOf(0) }


    LaunchedEffect(selectedVehicle) {
        selectedVehicle?.let { vehicle ->
            capacity = vehicle.capacity.toString()
            selectedCargoType = when (vehicle.vehicleType.lowercase()) {
                "frigo", "frigofirik" -> "Frigofirik"
                "açık kasa" -> "Açık Kasa"
                "tenteli" -> "Tenteli"
                "tanker" -> "Tanker"
                else -> "Diğer"
            }
        }
    }

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
                                        location = location,
                                        date = date,
                                        capacity = capacity,
                                        cargoType = selectedCargoType,
                                    )
                                )
                                isEditing = false
                            }) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Kaydet",
                                    tint = Color.White
                                )
                            }
                        } else {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Düzenle",
                                    tint = Color.White
                                )
                            }
                        }
                        IconButton(onClick = { onDeleteClick?.invoke() }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = DarkGray
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 20.dp,top = 8.dp, end = 20.dp, bottom = 20.dp)
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

                if (vehicleViewModel.myVehicles.value.isNotEmpty()) {
                    Text(
                        text = "Araçlarım (${vehicleViewModel.myVehicles.value.size})",
                        color = RoseRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { showVehiclePicker = true }
                    )
                }
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Taşıma Kapasitesi (ton)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
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
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
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
                        val countryName =
                            geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                        location = "$cityName, $countryName"
                    }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Tarih") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openDatePicker.value = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledContainerColor = Color.Transparent,
                        disabledLabelColor = Color.Black,
                        disabledBorderColor = Color.Gray
                    )
                )

                // 🔹 Date Picker Dialog
                if (openDatePicker.value) {
                    DatePickerDialog(
                        onDismissRequest = { openDatePicker.value = false },
                        confirmButton = {
                            TextButton(onClick = {
                                openDatePicker.value = false
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val formattedDate =
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                            Date(millis)
                                        )
                                    date = formattedDate
                                }
                            }) {
                                Text("Tamam")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openDatePicker.value = false }) {
                                Text("İptal")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                if (showVehiclePicker) {
                    AlertDialog(
                        onDismissRequest = { showVehiclePicker = false },
                        confirmButton = {},
                        title = { Text("Araç Seç") },
                        text = {
                            Column {
                                vehicleViewModel.myVehicles.collectAsState().value.forEach { vehicle ->
                                    Text(
                                        text = "${vehicle.title} - ${vehicle.licensePlate}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                vehicleViewModel.fetchVehicleById(vehicle.id, context)
                                                showVehiclePicker = false
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    )
                }

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
                            cargoType = cargoType,
                            capacity = capacity,
                            location = location,
                            date = date
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
