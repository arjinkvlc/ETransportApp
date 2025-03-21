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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.etransportapp.R
import com.example.etransportapp.ui.theme.LightBlue

@Composable
fun IntroScreen() {
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
                painter = painterResource(id = R.drawable.icon_logo),
                contentDescription = "Logo Image"
            )
            Image(
                painter = painterResource(id = R.drawable.e_tasimacilik),
                contentDescription = "E-Tasimacilik"
            )
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( containerColor = LightBlue),
            onClick = { /*TODO*/ }) {
            Text(text = "Giriş Yap")
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( containerColor = LightBlue),
            onClick = { /*TODO*/ }) {
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
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun IntroScreenPreview() {
    IntroScreen()
}