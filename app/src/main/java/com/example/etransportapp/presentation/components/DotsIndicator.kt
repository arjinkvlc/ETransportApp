package com.example.etransportapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color.LightGray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalDots) { index ->
            Surface(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == selectedIndex) 12.dp else 8.dp)
                    .clip(CircleShape),
                color = if (index == selectedIndex) selectedColor else unselectedColor
            ) {}
        }
    }
}
