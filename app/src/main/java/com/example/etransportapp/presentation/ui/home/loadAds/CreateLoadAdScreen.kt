package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.data.model.ad.CargoAdCreateRequest
import com.example.etransportapp.data.remote.api.GeoPlace
import com.example.etransportapp.presentation.components.StepIndicator
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants
import com.example.etransportapp.util.PreferenceHelper
import com.example.etransportapp.util.VehicleTypeMapUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLoadAdScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadAdViewModel
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var price by remember { mutableStateOf(TextFieldValue()) }
    var date by remember { mutableStateOf(TextFieldValue()) }
    var weight by remember { mutableStateOf(TextFieldValue()) }

    var selectedOriginPlace by remember { mutableStateOf<GeoPlace?>(null) }
    var selectedDestinationPlace by remember { mutableStateOf<GeoPlace?>(null) }
    var selectedCargoType by remember { mutableStateOf(VehicleTypeMapUtil.vehicleTypeLabels.first()) }
    var selectedCurrency by remember { mutableStateOf("TRY") }

    val isCargoTypeMenuExpanded = remember { mutableStateOf(false) }
    val currencies = listOf("TRY", "USD", "EUR")
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val suggestedPrice by viewModel.suggestedPriceText

    val geoNamesViewModel: GeoNamesViewModel = viewModel()
    val focusManager = LocalFocusManager.current

    var currentStep by remember { mutableStateOf(1) }
    var stepOneError by remember { mutableStateOf("") }
    var stepTwoError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Yük İlanı Oluştur", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == 1) {
                            navController.popBackStack()
                        } else {
                            currentStep = 1
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = DarkGray,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (currentStep == 1) {
                            val isValid = title.text.isNotBlank() &&
                                    description.text.isNotBlank() &&
                                    weight.text.isNotBlank() &&
                                    selectedOriginPlace != null &&
                                    selectedDestinationPlace != null

                            if (isValid) {
                                stepOneError = ""
                                viewModel.fetchSuggestedPrice(
                                    pickCountry = selectedOriginPlace?.countryName ?: "",
                                    pickCity = selectedOriginPlace?.name ?: "",
                                    dropCountry = selectedDestinationPlace?.countryName ?: "",
                                    dropCity = selectedDestinationPlace?.name ?: "",
                                    weight = weight.text.toDoubleOrNull() ?: 0.0,
                                    cargoType = VehicleTypeMapUtil.getEnumValueFromLabel(
                                        selectedCargoType
                                    ) ?: "Others"
                                )
                                currentStep = 2
                            } else {
                                stepOneError = "Lütfen tüm alanları doldurunuz."
                            }
                        } else {
                            if (price.text.isBlank() || date.text.isBlank()) {
                                stepTwoError = "Lütfen fiyat ve yükleme tarihini giriniz."
                            } else {
                                stepTwoError = ""
                                val request = CargoAdCreateRequest(
                                    userId = PreferenceHelper.getUserId(context) ?: "",
                                    title = title.text,
                                    description = description.text,
                                    weight = weight.text.toIntOrNull() ?: 0,
                                    cargoType = VehicleTypeMapUtil.getEnumValueFromLabel(selectedCargoType) ?: "Others",
                                    dropCountry = selectedDestinationPlace?.countryName ?: "",
                                    dropCity = selectedDestinationPlace?.name ?: "",
                                    pickCountry = selectedOriginPlace?.countryName ?: "",
                                    pickCity = selectedOriginPlace?.name ?: "",
                                    currency = selectedCurrency,
                                    price = price.text.toIntOrNull() ?: 0
                                )
                                viewModel.createCargoAd(
                                    request = request,
                                    onSuccess = { navController.popBackStack() },
                                    onError = { }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed)
                ) {
                    Text(if (currentStep == 1) "İlerle" else "İlanı Oluştur")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StepIndicator(currentStep = currentStep)
            HorizontalDivider(modifier = Modifier.height(1.dp))
            if (currentStep == 1) {
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

                Text(
                    "Yükleme Noktası",
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGray
                )
                CountryCitySelector(Constants.GEO_NAMES_USERNAME, geoNamesViewModel) { code, city ->
                    val place = geoNamesViewModel.cities.value.find { it.name == city }
                    selectedOriginPlace = place
                }

                Text("Varış Noktası", style = MaterialTheme.typography.titleSmall, color = DarkGray)
                CountryCitySelector(Constants.GEO_NAMES_USERNAME, geoNamesViewModel) { code, city ->
                    val place = geoNamesViewModel.cities.value.find { it.name == city }
                    selectedDestinationPlace = place
                }

                ExposedDropdownMenuBox(
                    expanded = isCargoTypeMenuExpanded.value,
                    onExpandedChange = { isCargoTypeMenuExpanded.value = it }) {
                    OutlinedTextField(
                        value = selectedCargoType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Yük Tipi") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCargoTypeMenuExpanded.value)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCargoTypeMenuExpanded.value,
                        onDismissRequest = { isCargoTypeMenuExpanded.value = false }) {
                        VehicleTypeMapUtil.vehicleTypeLabels.forEach { label ->
                            DropdownMenuItem(text = { Text(label) }, onClick = {
                                selectedCargoType = label
                                isCargoTypeMenuExpanded.value = false
                            })
                        }
                    }
                }

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it.copy(text = it.text.replace(",", ".")) },
                    label = { Text("Yük Ağırlığı (ton)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                if (stepOneError.isNotEmpty()) {
                    Text(
                        stepOneError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                if (!suggestedPrice.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = RoseRed
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Önerilen Fiyat Aralığı",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                            )
                            Text(
                                text = suggestedPrice!!,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Fiyat") },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged {
                                if (!it.isFocused) focusManager.clearFocus()
                            },
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

                    Spacer(modifier = Modifier.width(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = isCurrencyMenuExpanded,
                        onExpandedChange = { isCurrencyMenuExpanded = !isCurrencyMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCurrency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Birim") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCurrencyMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .widthIn(min = 96.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = isCurrencyMenuExpanded,
                            onDismissRequest = { isCurrencyMenuExpanded = false }
                        ) {
                            currencies.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        selectedCurrency = currency
                                        isCurrencyMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Yükleme Tarihi") },
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

                if (stepTwoError.isNotEmpty()) {
                    Text(stepTwoError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
