package com.example.etransportapp.presentation.ui.home.profile.myVehicles

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.data.model.Vehicle
import com.example.etransportapp.presentation.components.VehicleDialog
import com.example.etransportapp.presentation.viewModels.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehiclesScreen(
    navController: NavHostController,
    vehicleViewModel: VehicleViewModel = viewModel()
) {
    val vehicles by vehicleViewModel.myVehicles.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var vehicleToEdit by remember { mutableStateOf<Vehicle?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Araçlarım") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        vehicleToEdit = null
                        showDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Araç Ekle")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (vehicles.isEmpty()) {
                Text("Henüz eklenmiş araç yok.", style = MaterialTheme.typography.bodyLarge)
            } else {
                vehicles.forEach { vehicle ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(vehicle.name, fontWeight = FontWeight.Bold)
                                Text("Tür: ${vehicle.vehicleType}")
                                Text("Kapasite: ${vehicle.capacity} kg")
                                Text("Plaka: ${vehicle.plate}")
                                Text("Model: ${vehicle.model}")
                            }

                            Row {
                                IconButton(onClick = {
                                    vehicleToEdit = vehicle
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Düzenle")
                                }
                                IconButton(onClick = {
                                    vehicleViewModel.deleteVehicle(vehicle)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Sil")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            VehicleDialog(
                initialVehicle = vehicleToEdit,
                onDismiss = {
                    showDialog = false
                    vehicleToEdit = null
                },
                onSave = { vehicle ->
                    if (vehicleToEdit == null) {
                        vehicleViewModel.addVehicle(vehicle)
                    } else {
                        vehicleViewModel.updateVehicle(vehicle)
                    }
                    showDialog = false
                    vehicleToEdit = null
                }
            )
        }
    }
}
