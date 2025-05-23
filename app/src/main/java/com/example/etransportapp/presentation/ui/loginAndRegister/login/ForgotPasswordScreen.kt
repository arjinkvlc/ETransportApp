package com.example.etransportapp.presentation.ui.loginAndRegister.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ForgotPasswordViewModel = viewModel()
    var step by remember { mutableStateOf(1) }
    var verificationCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Close icon
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Şifremi Unuttum", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        if (step == 1) {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("E-posta") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.generateToken(context) { step = 2 }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(RoseRed)
            ) {
                Text("Kod Gönder", fontSize = 16.sp)
            }
        } else {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Doğrulama Kodu") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Yeni Şifre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                label = { Text("Yeni Şifre (Tekrar)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.resetPassword(
                        context,
                        token = verificationCode,
                        newPassword = newPassword,
                        confirmPassword = confirmNewPassword
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Şifreyi Güncelle", fontSize = 16.sp)
            }
        }
    }
}

