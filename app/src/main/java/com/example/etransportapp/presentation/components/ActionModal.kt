package com.example.etransportapp.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.etransportapp.R
import com.example.etransportapp.presentation.navigation.NavRoutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FloatingActionMenu(
    show: Boolean,
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var internalVisible by remember { mutableStateOf(false) }

    val loadOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val vehicleOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    LaunchedEffect(show) {
        if (show) {
            internalVisible = true

            loadOffset.snapTo(Offset(0f, 0f))
            vehicleOffset.snapTo(Offset(0f, 0f))

            launch {
                loadOffset.animateTo(
                    Offset(-100f, -100f),
                    animationSpec = tween(durationMillis = 300)
                )
            }
            launch {
                vehicleOffset.animateTo(
                    Offset(100f, -100f),
                    animationSpec = tween(durationMillis = 300)
                )
            }

        } else {
            launch {
                loadOffset.animateTo(
                    Offset(0f, 0f),
                    animationSpec = tween(durationMillis = 300)
                )
            }
            launch {
                vehicleOffset.animateTo(
                    Offset(0f, 0f),
                    animationSpec = tween(durationMillis = 300)
                )
            }

            delay(300)
            internalVisible = false
        }
    }

    if (!internalVisible && !show) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = if (show) 0.4f else 0f))
            .clickable(enabled = show) { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = loadOffset.value.x.dp, y = loadOffset.value.y.dp)
        ) {
            FloatingActionButtonItem(
                icon = painterResource(id = R.drawable.ic_loadads),
                label = "Yük İlanı",
                onClick = {
                    onNavigate(NavRoutes.CREATE_LOAD_AD)
                }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = vehicleOffset.value.x.dp, y = vehicleOffset.value.y.dp)
        ) {
            FloatingActionButtonItem(
                icon = painterResource(id = R.drawable.ic_vehicleads),
                label = "Araç İlanı",
                onClick = {
                    onNavigate(NavRoutes.CREATE_VEHICLE_AD)
                }
            )
        }
    }
}
