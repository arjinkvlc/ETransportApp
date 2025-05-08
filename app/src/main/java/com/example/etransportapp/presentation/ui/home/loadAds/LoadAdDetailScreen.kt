package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.presentation.components.InfoText
import com.example.etransportapp.presentation.viewModels.GeoNamesViewModel
import com.example.etransportapp.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.presentation.components.CountryCitySelector
import com.example.etransportapp.util.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadAdDetailScreen(
    loadAd: LoadAd,
    navController: NavHostController,
    isMyAd: Boolean = loadAd.userId == "username",
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((LoadAd) -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(loadAd.title) }
    var description by remember { mutableStateOf(loadAd.description) }
    var origin by remember { mutableStateOf(loadAd.origin) }
    var destination by remember { mutableStateOf(loadAd.destination) }
    var price by remember { mutableStateOf(loadAd.price) }
    var date by remember { mutableStateOf(loadAd.date) }
    var weight by remember { mutableStateOf(loadAd.weight) }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val geoNamesViewModel: GeoNamesViewModel = viewModel()

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
                                onUpdateClick?.invoke(
                                    loadAd.copy(
                                        title = title,
                                        description = description,
                                        origin = origin,
                                        destination = destination,
                                        price = price,
                                        date = date,
                                        weight = weight
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
                CountryCitySelector(
                    labelPrefix = "Yükleme Noktası",
                    username = Constants.GEO_NAMES_USERNAME,
                    geoViewModel = geoNamesViewModel,
                    onSelected = { countryCode, cityName ->
                        val countryName = geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                        origin = "$cityName, $countryName"
                    }
                )

                CountryCitySelector(
                    labelPrefix = "Varış Noktası",
                    username = Constants.GEO_NAMES_USERNAME,
                    geoViewModel = geoNamesViewModel,
                    onSelected = { countryCode, cityName ->
                        val countryName = geoNamesViewModel.countries.value.find { it.countryCode == countryCode }?.countryName.orEmpty()
                        destination = "$cityName, $countryName"
                    }
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Yük Ağırlığı (ton)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Fiyat") },
                    modifier = Modifier.fillMaxWidth()
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

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                InfoText("Başlık", title)
                InfoText("Yükleme Noktası", origin)
                InfoText("Varış Noktası", destination)
                InfoText("Yük Ağırlığı ", "$weight ton")
                InfoText("Fiyat", "$price ₺")
                InfoText("Tarih", date)
                InfoText("Açıklama", description)
            }
        }
    }
}
