package com.example.etransportapp.presentation.ui.home.loadAds

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
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var weight by remember { mutableStateOf(TextFieldValue("")) }

    var origin by remember { mutableStateOf("") }
    var selectedOriginPlace by remember { mutableStateOf<GeoPlace?>(null) }
    var selectedDestinationPlace by remember { mutableStateOf<GeoPlace?>(null) }
    var destination by remember { mutableStateOf("") }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedCargoType by remember { mutableStateOf(VehicleTypeMapUtil.vehicleTypeLabels.first()) }
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val currencies = listOf("TRY", "USD", "EUR")
    var selectedCurrency by remember { mutableStateOf("TRY") }
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }
    var suggestedPriceText by remember { mutableStateOf<String?>(null) }


    val geoNamesViewModel: GeoNamesViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Yük İlanı Oluştur", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (weight.text.contains(",")) {
                            weight = weight.copy(text = weight.text.replace(",", "."))
                        }

                        val pickCity = selectedOriginPlace?.name ?: ""
                        val pickCountry = selectedOriginPlace?.countryName ?: ""
                        val dropCity = selectedDestinationPlace?.name ?: ""
                        val dropCountry = selectedDestinationPlace?.countryName ?: ""

                        val request = CargoAdCreateRequest(
                            userId = PreferenceHelper.getUserId(context) ?: "",
                            title = title.text,
                            description = description.text,
                            weight = weight.text.toIntOrNull() ?: 0,
                            cargoType = VehicleTypeMapUtil.getEnumValueFromLabel(selectedCargoType)
                                ?: "Others",
                            dropCountry = dropCountry,
                            dropCity = dropCity,
                            pickCountry = pickCountry,
                            pickCity = pickCity,
                            currency = selectedCurrency,
                            price = price.text.toIntOrNull() ?: 0
                        )

                        viewModel.createCargoAd(
                            request = request,
                            onSuccess = {
                                navController.popBackStack()
                            },
                            onError = {
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
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

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
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

            Text(
                text = "Yükleme Noktası",
                color = DarkGray,
                style = MaterialTheme.typography.titleSmall
            )
            CountryCitySelector(
                username = Constants.GEO_NAMES_USERNAME,
                geoViewModel = geoNamesViewModel,
                onSelected = { countryCode, cityName ->
                    val place = geoNamesViewModel.cities.value.find { it.name == cityName }
                    selectedOriginPlace = place
                    val countryName =
                        geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                    origin = "$cityName, $countryName"
                }
            )
            Text(
                text = "Varış Noktası",
                color = DarkGray,
                style = MaterialTheme.typography.titleSmall
            )
            CountryCitySelector(
                username = Constants.GEO_NAMES_USERNAME,
                geoViewModel = geoNamesViewModel,
                onSelected = { countryCode, cityName ->
                    val place = geoNamesViewModel.cities.value.find { it.name == cityName }
                    selectedDestinationPlace = place
                    val countryName =
                        geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                    destination = "$cityName, $countryName"
                }
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
                    VehicleTypeMapUtil.vehicleTypeLabels.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedCargoType = label
                                isCargoTypeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Yük Ağırlığı (ton)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        weight = weight.copy(text = weight.text.replace(",", "."))
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

            if (selectedOriginPlace != null && selectedDestinationPlace != null && weight.text.isNotEmpty() && weight.text.toDoubleOrNull() != null) {
                LaunchedEffect(selectedOriginPlace, selectedDestinationPlace, weight.text) {
                    val origin = "${selectedOriginPlace?.name}, ${selectedOriginPlace?.countryName}"
                    val destination = "${selectedDestinationPlace?.name}, ${selectedDestinationPlace?.countryName}"
                    val weightValue = weight.text.toDouble()
                     suggestedPriceText = viewModel.fetchSuggestedPrice(
                         pickCountry = selectedOriginPlace?.countryName ?: "",
                        pickCity = "",
                        dropCountry = "",
                        dropCity = "",
                         weight = weightValue,
                        VehicleTypeMapUtil.getEnumValueFromLabel(selectedCargoType) ?: "Others"
                    ).value
                }
                Text(
                    text = suggestedPriceText ?: "Önerilen Fiyat: Hesaplanıyor...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
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
        }
    }
}
