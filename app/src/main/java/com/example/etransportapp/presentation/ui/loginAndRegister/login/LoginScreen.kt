package com.example.etransportapp.presentation.ui.loginAndRegister.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.DarkGray


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    modifier: Modifier,
    navController: NavHostController
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.FillHeight
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.text_etasimacilik),
                contentDescription = "App text",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hoş Geldiniz",
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("E-posta", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.White,
                disabledTextColor = Color.White
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Şifre", color = Color.White) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    //val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    //Image(painter = painterResource(id = icon), contentDescription = "Toggle Password")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.White,
                disabledTextColor = Color.White
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
                )
                Text("Beni Hatırla", color = Color.White)
            }

            Text(
                text = "Şifremi Unuttum?",
                color = DarkGray,
                modifier = Modifier.clickable { /* Şifremi unuttum işlemi */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.email.isNotEmpty() && viewModel.password.isNotEmpty()) {
                    viewModel.login(context) {
                        navController.navigate(NavRoutes.LOAD_ADS) {
                            popUpTo(NavRoutes.LOGIN) { inclusive = true }
                        }
                    }
                } else {
                    Toast.makeText(context, "E-posta ve şifre boş olamaz", Toast.LENGTH_SHORT).show()
                }
            },
        ) {
        Text("Giriş Yap", fontSize = 18.sp)
    }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hesabın yok mu? Kayıt Ol",
            color = DarkGray,
            modifier = Modifier.clickable { }
        )
    }
}

