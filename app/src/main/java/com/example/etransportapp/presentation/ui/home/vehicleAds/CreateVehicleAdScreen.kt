package com.example.etransportapp.presentation.ui.home.vehicleAds

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.VehicleAd
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants
import java.text.SimpleDateFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.data.model.ad.VehicleAdRequest
import com.example.etransportapp.util.PreferenceHelper
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVehicleAdScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: VehicleAdViewModel,
    vehicleViewModel: VehicleViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vehicleViewModel.fetchVehiclesByUser(context)
    }


    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember {
        mutableStateOf(TextFieldValue(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())))
    }
    var capacity by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf("") }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val geoNamesViewModel: GeoNamesViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    var showVehiclePicker by remember { mutableStateOf(false) }

    val cargoTypes = listOf("Açık Kasa", "Tenteli", "Frigofirik", "Tanker", "Diğer")
    var selectedCargoType by remember { mutableStateOf("Açık Kasa") }
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }

    var selectedCity by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }

    val selectedVehicle by vehicleViewModel.selectedVehicleById.collectAsState()

    LaunchedEffect(selectedVehicle) {
        selectedVehicle?.let { vehicle ->
            capacity = TextFieldValue(vehicle.capacity.toString())
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
                title = { Text("Yeni Araç İlanı Oluştur", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = DarkGray,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val userId = PreferenceHelper.getUserId(context)
                        if (
                            title.text.isNotBlank() &&
                            description.text.isNotBlank() &&
                            selectedCity.isNotBlank() &&
                            selectedCountry.isNotBlank() &&
                            userId != null
                        ) {
                            val request = VehicleAdRequest(
                                title = title.text,
                                description = description.text,
                                city = selectedCity,
                                country = selectedCountry,
                                carrierId = userId,
                                vehicleType = selectedCargoType,
                                capacity = capacity.text.toIntOrNull() ?: 0
                            )

                            viewModel.createVehicleAd(
                                context = context,
                                request = request,
                                onSuccess = { navController.popBackStack() },
                                onError = { errorMsg ->
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed)
                ) {
                    Text("İlanı Oluştur")
                }
            }
        }
    ) { innerPadding ->

        if (openDatePicker.value) {
            DatePickerDialog(
                onDismissRequest = { openDatePicker.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        openDatePicker.value = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
                            date = TextFieldValue(formattedDate)
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

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                    selectedCity = cityName
                    selectedCountry = geoNamesViewModel.countries.value
                        .find { it.countryCode == countryCode }?.countryName.orEmpty()
                    location = "$cityName, $selectedCountry"
                }

            )

            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Tarih") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openDatePicker.value = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledContainerColor = Color.Transparent,
                    disabledBorderColor = Color.Black,
                    disabledLabelColor = Color.Black
                ),
                enabled = false
            )

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
        }
    }
}
