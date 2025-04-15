package com.example.etransportapp.presentation.ui.home.loads

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.LoadItem
import com.example.etransportapp.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLoadScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoadViewModel// ViewModel'i burada da kullan
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var origin by remember { mutableStateOf(TextFieldValue("")) }
    var destination by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }

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
                    containerColor = LightBlue,
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
                            viewModel.addLoad(
                                LoadItem(
                                    title = title.text,
                                    description = description.text,
                                    origin = origin.text,
                                    destination = destination.text,
                                    price = price.text,
                                    date = date.text
                                )
                            )
                            navController.popBackStack() // oluşturma sonrası geri dön
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                    Text("İlanı Oluştur")
                }
            }
        }
    ) { innerPadding ->
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
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Yükleme Tarihi (GG/AA/YYYY)") }, modifier = Modifier.fillMaxWidth())
        }
    }
}
