package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.etransportapp.data.model.Vehicle
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun VehicleDialog(
    initialVehicle: Vehicle? = null,
    onDismiss: () -> Unit,
    onSave: (Vehicle) -> Unit
) {
    var name by remember { mutableStateOf(initialVehicle?.name.orEmpty()) }
    var type by remember { mutableStateOf(initialVehicle?.vehicleType.orEmpty()) }
    var capacity by remember { mutableStateOf(initialVehicle?.capacity?.toString().orEmpty()) }
    var plate by remember { mutableStateOf(initialVehicle?.plate.orEmpty()) }
    var model by remember { mutableStateOf(initialVehicle?.model.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(RoseRed)
                    ),
                    modifier = Modifier.width(128.dp)
                ) {
                    Text(
                        "İptal",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = RoseRed,
                    )
                }
                Button(
                    modifier = Modifier.width(128.dp),
                    onClick = {
                        if (name.isNotBlank() && type.isNotBlank() && capacity.isNotBlank() && plate.isNotBlank() && model.isNotBlank()) {
                            val vehicle = initialVehicle?.copy(
                                name = name,
                                vehicleType = type,
                                capacity = capacity.toIntOrNull() ?: 0,
                                plate = plate,
                                model = model
                            ) ?: Vehicle(
                                name = name,
                                vehicleType = type,
                                capacity = capacity.toIntOrNull() ?: 0,
                                plate = plate,
                                model = model
                            )
                            onSave(vehicle)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed),
                ) {
                    Text("Kaydet")
                }
            }
        },
        title = {
            Text(
                if (initialVehicle == null) "Araç Ekle" else "Araç Düzenle",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Başlık") })
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tür") })
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Kapasite (ton)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Plaka") })
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") })
            }
        }
    )
}
