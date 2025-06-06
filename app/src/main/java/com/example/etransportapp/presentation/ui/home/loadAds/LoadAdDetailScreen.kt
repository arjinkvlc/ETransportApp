package com.example.etransportapp.presentation.ui.home.loadAds

import android.widget.Toast
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.data.model.ad.CargoAdResponse
import com.example.etransportapp.data.model.ad.CargoAdUpdateRequest
import com.example.etransportapp.data.model.offer.CargoOfferRequest
import com.example.etransportapp.presentation.components.AdDetailTabRow
import com.example.etransportapp.presentation.components.AdOwnerInfoSection
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.presentation.components.LoadAdDetailSection
import com.example.etransportapp.presentation.components.LoadOfferDialog
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.Constants
import com.example.etransportapp.util.PreferenceHelper
import com.example.etransportapp.util.VehicleTypeMapUtil


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadAdDetailScreen(
    loadAd: CargoAdResponse,
    navController: NavHostController,
    isMyAd: Boolean = loadAd.userId == "username",
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((CargoAdUpdateRequest) -> Unit)? = null,
    ) {

    val context = LocalContext.current

    val viewModel: LoadAdViewModel = viewModel()
    LaunchedEffect(loadAd.userId) {
        viewModel.fetchAdOwnerInfo(loadAd.userId)
    }
    val adOwner = viewModel.adOwnerInfo.value

    val userId = PreferenceHelper.getUserId(context) ?: ""
    LaunchedEffect(userId) {
        viewModel.fetchSentOffers(userId)
    }
    val sentOffers = viewModel.sentOffers.value
    val existingOffer = sentOffers.find { it.cargoAdId == loadAd.id && it.status == "Pending" }

    val isOfferButtonEnabled = existingOffer == null
    val offerButtonText = if (isOfferButtonEnabled) "Teklif Ver" else "Teklif Beklemede"


    var isEditing by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(loadAd.title) }
    var description by remember { mutableStateOf(loadAd.description) }
    var origin by remember { mutableStateOf("${loadAd.pickCity}, ${loadAd.pickCountry}") }
    var destination by remember { mutableStateOf("${loadAd.dropCity}, ${loadAd.dropCountry}") }
    var price by remember { mutableStateOf(loadAd.price.toString()) }
    var date by remember { mutableStateOf(loadAd.adDate.substring(0, 10)) }
    var weight by remember { mutableStateOf(loadAd.weight.toString()) }
    var currency by remember { mutableStateOf(loadAd.currency) }
    val currencies = listOf("TRY", "USD", "EUR")
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var selectedCargoType by remember { mutableStateOf(loadAd.cargoType) ?: mutableStateOf("Açık Kasa") }
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }
    var offerMessage by remember { mutableStateOf("") }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val geoNamesViewModel: GeoNamesViewModel = viewModel()
    var showOfferDialog by remember { mutableStateOf(false) }
    var offerPrice by remember { mutableStateOf("") }
    val tabs = listOf("İlan Detayı", "İlan Sahibi")
    var selectedTabIndex by remember { mutableStateOf(0) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yük İlan Detayı", color = Color.White) },
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
                                if (weight.contains(",")) {
                                    weight = weight.replace(",", ".")
                                }

                                onUpdateClick?.invoke(
                                    CargoAdUpdateRequest(
                                        id = loadAd.id,
                                        title = title,
                                        description = description,
                                        weight = weight.toIntOrNull() ?: 0,
                                        cargoType = selectedCargoType,
                                        price = price.toIntOrNull() ?: 0,
                                        isExpired = false,
                                        dropCountry = loadAd.dropCountry,
                                        dropCity = loadAd.dropCity,
                                        pickCountry = loadAd.pickCountry,
                                        pickCity = loadAd.pickCity,
                                        currency = currency
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
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
                        .onFocusChanged {
                            weight = weight.replace(",", ".")
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            value = currency,
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
                            currencies.forEach { curr ->
                                DropdownMenuItem(
                                    text = { Text(curr) },
                                    onClick = {
                                        currency = curr
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

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                AdDetailTabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )


                Spacer(modifier = Modifier.height(12.dp))

                when (selectedTabIndex) {
                    0 -> {
                        LoadAdDetailSection(
                            title = title,
                            description = description,
                            origin = origin,
                            destination = destination,
                            type = selectedCargoType,
                            weight = weight,
                            price = price,
                            currency = currency,
                            date = date
                        )
                    }

                    1 -> {
                        AdOwnerInfoSection(
                            name = "${adOwner?.name ?: ""} ${adOwner?.surname ?: ""}",
                            email = adOwner?.email ?: "E-posta yok",
                            phone = adOwner?.phoneNumber ?: "Telefon yok"
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                if (isMyAd) {
                    Button(
                        onClick = {
                            navController.navigate("loadAdOffers/${loadAd.id}")
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
                        onClick = { showOfferDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        enabled = isOfferButtonEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseRed,
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Text(offerButtonText)
                    }

                }
            }
            if (showOfferDialog) {
                LoadOfferDialog(
                    message = offerMessage,
                    currency = currency,
                    offerPrice = offerPrice,
                    onMessageChange = { offerMessage = it },
                    onPriceChange = { offerPrice = it },
                    onDismiss = {
                        showOfferDialog = false
                        offerPrice = ""
                        offerMessage = ""
                    },
                    onConfirm = {
                        val userId = PreferenceHelper.getUserId(context)
                        if (userId == null) {
                            Toast.makeText(context, "Kullanıcı kimliği bulunamadı", Toast.LENGTH_SHORT).show()
                            return@LoadOfferDialog
                        }

                        val expiry = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_YEAR, 2)
                        }.time
                        val formattedExpiry = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(expiry)

                        val request = CargoOfferRequest(
                            senderId = userId,
                            receiverId = loadAd.userId,
                            cargoAdId = loadAd.id,
                            price = offerPrice.toIntOrNull() ?: 0,
                            message = offerMessage,
                            expiryDate = formattedExpiry
                        )

                        viewModel.createCargoOffer(
                            request = request,
                            onSuccess = {
                                showOfferDialog = false
                                offerPrice = ""
                                offerMessage = ""
                                Toast.makeText(context, "Teklif gönderildi", Toast.LENGTH_SHORT).show()
                            },
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                )
            }


        }
    }
}

