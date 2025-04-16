package com.example.etransportapp.presentation.ui.home.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.presentation.ui.loginAndRegister.intro.IntroViewModel

@Composable
fun ProfileScreen(modifier: Modifier, navController: NavHostController) {
    val viewModel = IntroViewModel() // ViewModel çağrısı
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "PROFILE SCREEN")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            // Oturum bilgisini sil
            viewModel.saveLoginState(context, false)

            // Intro ekranına yönlendir, geri gelinmesin
            navController.navigate(NavRoutes.INTRO) {
                popUpTo(0) { inclusive = true }
            }
        }) {
            Text("Çıkış Yap")
        }
    }
}
