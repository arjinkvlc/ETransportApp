package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.example.etransportapp.data.model.vehicle.VehicleRequest
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun VehicleDialog(
    initialVehicle: VehicleRequest? = null,
    onDismiss: () -> Unit,
    onSave: (VehicleRequest) -> Unit
) {
    var title by remember { mutableStateOf(initialVehicle?.title.orEmpty()) }
    var type by remember { mutableStateOf(initialVehicle?.vehicleType.orEmpty()) }
    var capacity by remember { mutableStateOf(initialVehicle?.capacity?.toString().orEmpty()) }
    var plate by remember { mutableStateOf(initialVehicle?.licensePlate.orEmpty()) }
    var model by remember { mutableStateOf(initialVehicle?.model.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && type.isNotBlank() && capacity.isNotBlank() && plate.isNotBlank() && model.isNotBlank()) {
                        val request = VehicleRequest(
                            title = title,
                            vehicleType = type,
                            capacity = capacity.toIntOrNull() ?: 0,
                            licensePlate = plate,
                            model = model,
                            carrierId = initialVehicle?.carrierId.orEmpty() // boş geçilebilir, ViewModel'de override ediliyor
                        )
                        onSave(request)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseRed),
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = RoseRed)
            }
        },
        title = { Text(if (initialVehicle == null) "Araç Ekle" else "Araç Düzenle") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Başlık") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Tür") })
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Kapasite (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = plate, onValueChange = { plate = it }, label = { Text("Plaka") })
                OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model") })
            }
        }
    )
}
