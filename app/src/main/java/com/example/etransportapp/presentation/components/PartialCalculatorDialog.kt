package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.etransportapp.ui.theme.RoseRed
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialCalculatorDialog(
    onDismiss: () -> Unit
) {
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    var unitPrice by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("TRY") }
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }

    val calculatedResult = remember(length, width, height, weight, quantity, unitPrice, selectedCurrency) {
        val l = length.toDoubleOrNull() ?: 0.0
        val w = width.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        val wt = weight.toDoubleOrNull() ?: 0.0
        val q = quantity.toIntOrNull() ?: 0
        val pricePerUnit = unitPrice.toDoubleOrNull() ?: 0.0

        val volumeM3 = (l * w * h * q) / 1_000_000.0
        val totalWeightTon = (wt * q) / 1000.0

        val maxChargeable = maxOf(volumeM3, totalWeightTon)

        if (maxChargeable > 0 && pricePerUnit > 0) {
            "%.2f $selectedCurrency".format(maxChargeable * pricePerUnit)
        } else "-"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
        title = { Text("Parsiyel Ücret Hesaplama") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = length,
                    onValueChange = { length = it },
                    label = { Text("Uzunluk (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = width,
                    onValueChange = { width = it },
                    label = { Text("Genişlik (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Yükseklik (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Ağırlık (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Adet") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                    OutlinedTextField(
                        value = unitPrice,
                        onValueChange = { unitPrice = it },
                        label = { Text("Navlun Birim Fiyatı") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = isCurrencyMenuExpanded,
                        onExpandedChange = { isCurrencyMenuExpanded = !isCurrencyMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCurrency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Para Birimi") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCurrencyMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = isCurrencyMenuExpanded,
                            onDismissRequest = { isCurrencyMenuExpanded = false }
                        ) {
                            listOf("TRY", "USD", "EUR").forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        selectedCurrency = currency
                                        isCurrencyMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Tahmini Navlun Ücreti: $calculatedResult", fontWeight = FontWeight.Bold, color = RoseRed)
            }
        }
    )
}
