package com.example.etransportapp.presentation.ui.loginAndRegister.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray

@Composable
fun IntroScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: IntroViewModel = IntroViewModel()
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo Image"
            )
            Image(
                painter = painterResource(id = R.drawable.text_etasimacilik),
                contentDescription = "E-Tasimacilik"
            )
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
            onClick = {
                viewModel.saveLoginState(context,true)
                navController.navigate("load_ads") {
                    popUpTo("intro") { inclusive = true }
                }
            }
        ) {
            Text(text = "Giriş Yap")
        }

        // Kayıt Ol Butonu
        Button(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
            onClick = {
                navController.navigate(NavRoutes.REGISTER)
            }
        ) {
            Text(text = "Kayıt Ol")
        }

        Text(
            modifier = Modifier.padding(vertical = 20.dp),
            text = "Tır Arayanların Ve Tırcıların Adresi !",
            fontWeight = FontWeight.Bold
        )

        Image(
            painter = painterResource(id = R.drawable.intro_truck),
            contentDescription = "Truck Image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}


/*
@Preview
@Composable
fun IntroScreenPreview() {
    IntroScreen(modifier, navController)
}*/
