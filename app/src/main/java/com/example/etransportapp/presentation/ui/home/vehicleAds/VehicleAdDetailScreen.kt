package com.example.etransportapp.presentation.ui.home.vehicleAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.etransportapp.data.model.ad.VehicleAd
import com.example.etransportapp.presentation.components.InfoText
import com.example.etransportapp.ui.theme.DarkGray
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAdDetailScreen(
    vehicleAd: VehicleAd,
    navController: NavHostController,
    isMyAd: Boolean = vehicleAd.userId == "username",
    onDeleteClick: (() -> Unit)? = null,
    onUpdateClick: ((VehicleAd) -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(vehicleAd.title) }
    var description by remember { mutableStateOf(vehicleAd.description) }
    var location by remember { mutableStateOf(vehicleAd.location) }
    var date by remember { mutableStateOf(vehicleAd.date) }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
                                        date = date
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
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = DarkGray
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (isEditing) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Başlık") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Açıklama") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Konum") }, modifier = Modifier.fillMaxWidth())

                // 🔹 Date Picker Field
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
                                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
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

            } else {
                InfoText("Başlık", title)
                InfoText("Açıklama", description)
                InfoText("Konum", location)
                InfoText("Tarih", date)
            }
        }
    }
}
