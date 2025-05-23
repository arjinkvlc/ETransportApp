package com.example.etransportapp.presentation.ui.home.profile.myVehicles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.data.model.Vehicle
import com.example.etransportapp.presentation.components.VehicleDialog
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

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
                title = { Text("Araçlarım", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGray,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
            ))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vehicleToEdit = null
                    showDialog = true
                },
                containerColor = RoseRed,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Add, contentDescription = "Araç Ekle")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            if (vehicles.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.local_shipping_24px),
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Henüz eklenmiş araç yok",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Yeni bir araç eklemek için '+' butonuna basın.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                vehicles.forEach { vehicle ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(vehicle.name, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Tür: ${vehicle.vehicleType}", style = MaterialTheme.typography.bodyMedium)
                                Text("Kapasite: ${vehicle.capacity} kg", style = MaterialTheme.typography.bodyMedium)
                                Text("Plaka: ${vehicle.plate}", style = MaterialTheme.typography.bodyMedium)
                                Text("Model: ${vehicle.model}", style = MaterialTheme.typography.bodyMedium)
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        vehicleToEdit = vehicle
                                        showDialog = true
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Düzenle")
                                }
                                IconButton(
                                    onClick = {
                                        vehicleViewModel.deleteVehicle(vehicle)
                                    }
                                ) {
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
