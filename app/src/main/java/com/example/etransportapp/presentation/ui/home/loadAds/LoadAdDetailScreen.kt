package com.example.etransportapp.presentation.ui.home.loadAds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.LoadAd
import com.example.etransportapp.presentation.components.InfoText
import com.example.etransportapp.ui.theme.LightBlue

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Yük İlan Detayı", color = Color.White)
                },
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
                    containerColor = LightBlue
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
                OutlinedTextField(value = origin, onValueChange = { origin = it }, label = { Text("Yükleme Noktası") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = destination, onValueChange = { destination = it }, label = { Text("Varış Noktası") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Fiyat") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Tarih") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Açıklama") }, modifier = Modifier.fillMaxWidth())
            } else {
                InfoText("Başlık", title)
                InfoText("Yükleme Noktası", origin)
                InfoText("Varış Noktası", destination)
                InfoText("Fiyat", "$price ₺")
                InfoText("Tarih", date)
                InfoText("Açıklama", description)
            }
        }
    }
}


