package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.etransportapp.data.model.vehicle.VehicleRequest
import com.example.etransportapp.presentation.viewModels.VehicleViewModel
import com.example.etransportapp.ui.theme.RoseRed
import com.example.etransportapp.util.VehicleTypeMapUtil

@OptIn(ExperimentalMaterial3Api::class)
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
    var isCargoTypeMenuExpanded by remember { mutableStateOf(false) }

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
                    onClick = {
                        if (title.isNotBlank() && type.isNotBlank() && capacity.isNotBlank() && plate.isNotBlank() && model.isNotBlank()) {
                            val request = VehicleRequest(
                                title = title,
                                vehicleType = type,
                                capacity = capacity.toIntOrNull() ?: 0,
                                licensePlate = plate,
                                model = model,
                                carrierId = initialVehicle?.carrierId.orEmpty()
                            )
                            onSave(request)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed),
                    modifier = Modifier.width(128.dp)
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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") })

                ExposedDropdownMenuBox(
                    expanded = isCargoTypeMenuExpanded,
                    onExpandedChange = { isCargoTypeMenuExpanded = !isCargoTypeMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Yük Tipi") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCargoTypeMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCargoTypeMenuExpanded,
                        onDismissRequest = { isCargoTypeMenuExpanded = false }
                    ) {
                        VehicleTypeMapUtil.vehicleTypeLabels.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    type = label
                                    isCargoTypeMenuExpanded = false
                                }
                            )
                        }
                    }
                }
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