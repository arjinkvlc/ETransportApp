package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.ad.LoadAd
import com.example.etransportapp.ui.theme.DarkGray

import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLoadAdScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadAdViewModel
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var origin by remember { mutableStateOf(TextFieldValue("")) }
    var destination by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }

    val openDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Yük İlanı Oluştur", textAlign = TextAlign.Center) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (
                            title.text.isNotBlank() &&
                            description.text.isNotBlank() &&
                            origin.text.isNotBlank() &&
                            destination.text.isNotBlank() &&
                            price.text.isNotBlank() &&
                            date.text.isNotBlank()
                        ) {
                            viewModel.addLoadAd(
                                LoadAd(
                                    title = title.text,
                                    description = description.text,
                                    origin = origin.text,
                                    destination = destination.text,
                                    price = price.text,
                                    date = date.text,
                                    userId = "username"
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGray)
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
                            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Başlık") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Açıklama") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = origin, onValueChange = { origin = it }, label = { Text("Yükleme Noktası") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = destination, onValueChange = { destination = it }, label = { Text("Varış Noktası") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Fiyat (₺)") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Yükleme Tarihi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openDatePicker.value = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledContainerColor = Color.Transparent, disabledBorderColor = Color.Black,
                    disabledLabelColor = Color.Black
                ),
                enabled = false
            )
        }
    }
}
