package com.example.etransportapp.presentation.ui.home.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.presentation.components.PartialCalculatorDialog
import com.example.etransportapp.presentation.components.ProfileInfoRow
import com.example.etransportapp.presentation.components.ProfileMenuItem
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.util.PreferenceHelper

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsState()
    var showPartialCalculator by remember { mutableStateOf(false) }


    LaunchedEffect(true) {
        viewModel.fetchUserProfile(context)
        val userId = PreferenceHelper.getUserId(context)
        if (!userId.isNullOrEmpty()) {
            viewModel.fetchAllOffers(userId)
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = DarkGray,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = profile?.name?.firstOrNull()?.toString() ?: "?",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${profile?.name ?: "..."} ${profile?.surname ?: ""}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text("İletişim Bilgileri", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            ProfileInfoRow(icon = R.drawable.baseline_phone_24, text = profile?.phoneNumber ?: "-")
            ProfileInfoRow(icon = R.drawable.baseline_email_24, text = profile?.email ?: "-")



            Spacer(Modifier.height(32.dp))

            Text("Hesap", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(text = "Araçlarım") {
                navController.navigate(NavRoutes.MY_VEHICLES)
            }
            ProfileMenuItem(text = "Parsiyel Hesaplama") {
                showPartialCalculator = true
            }
            ProfileMenuItem(text = "Gönderdiğim Teklifler") {
                navController.navigate(NavRoutes.SENT_OFFERS)
            }
            ProfileMenuItem(text = "Gelen Teklifler") {
                navController.navigate(NavRoutes.RECEIVED_OFFERS)
            }


            Spacer(Modifier.height(32.dp))

            Text("Destek", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(text = "Yardım Merkezi", onClick = { })
            ProfileMenuItem(text = "Kullanım Koşulları", onClick = { })
            ProfileMenuItem(text = "Gizlilik Politikası", onClick = { })
        }

        OutlinedButton(
            onClick = {
                PreferenceHelper.logout(navController.context)
                navController.navigate(NavRoutes.INTRO) {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "logout icon",
                tint = Color.Red,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Çıkış Yap")
        }

        if (showPartialCalculator) {
            PartialCalculatorDialog(onDismiss = { showPartialCalculator = false })
        }
    }
}


