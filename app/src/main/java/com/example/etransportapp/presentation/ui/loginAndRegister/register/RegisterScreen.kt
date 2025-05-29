package com.example.etransportapp.presentation.ui.loginAndRegister.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.ui.theme.DarkGray
import com.example.etransportapp.ui.theme.RoseRed

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(1) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var disclaimerAccepted by remember { mutableStateOf(false) }
    var showDisclaimerDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = if (step == 1) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (step == 1) navController.popBackStack() else step--
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Kayıt Ol", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            StepIndicator(currentStep = step)
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                },
                label = ""
            ) { currentStep ->
                when (currentStep) {
                    1 -> Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = viewModel.firstName,
                            onValueChange = { viewModel.firstName = it },
                            label = { Text("Ad") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.lastName,
                            onValueChange = { viewModel.lastName = it },
                            label = { Text("Soyad") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.birthYear,
                            onValueChange = { viewModel.birthYear = it },
                            label = { Text("Doğum Yılı") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(RoseRed)
                        ) {
                            Text("Devam Et")
                        }
                    }

                    2 -> Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = { Text("E-posta") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.phoneNumber,
                            onValueChange = { viewModel.phoneNumber = it },
                            label = { Text("Telefon") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = viewModel.userName,
                            onValueChange = { viewModel.userName = it },
                            label = { Text("Kullanıcı Adı") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            label = { Text("Şifre") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(id = if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                        tint = Color.Gray,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.confirmPassword,
                            onValueChange = { viewModel.confirmPassword = it },
                            label = { Text("Şifreyi Onayla") },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (confirmPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                        tint = Color.Gray,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DisclaimerCheckboxSection(
                            checked = disclaimerAccepted,
                            onCheckedChange = {
                                if (!disclaimerAccepted) showDisclaimerDialog =
                                    true else disclaimerAccepted = false
                            }
                        )
                        Button(
                            onClick = {
                                viewModel.registerUser(context) {
                                    step = 3
                                }
                            },
                            enabled = disclaimerAccepted,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(RoseRed)
                        ) {
                            Text("Devam Et")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    3 -> Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = verificationCode,
                            onValueChange = { verificationCode = it },
                            label = { Text("Doğrulama Kodu") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Kod gelmedi mi? Tekrar Gönder",
                            color = DarkGray,
                            modifier = Modifier
                                .clickable { viewModel.resendConfirmationCode(context) }
                                .align(Alignment.End),
                            fontWeight = FontWeight.Bold,
                        )

                        Button(
                            onClick = {
                                viewModel.verificationCode = verificationCode
                                viewModel.confirmEmail(
                                    context = context
                                ) {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(RoseRed)
                        ) {
                            Text("Kayıt Ol")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        Image(
            painter = painterResource(id = R.drawable.intro_truck),
            contentDescription = "Truck Image",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .then(Modifier.alpha(0.15f)),
            contentScale = ContentScale.Crop
        )

        if (showDisclaimerDialog) {
            DisclaimerDialog(
                onDismiss = { showDisclaimerDialog = false },
                onAccept = {
                    disclaimerAccepted = true
                    showDisclaimerDialog = false
                }
            )
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (1..3).forEach {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(4.dp)
                    .background(
                        color = if (it <= currentStep) RoseRed else Color.LightGray,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun DisclaimerCheckboxSection(
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = CheckboxDefaults.colors(checkedColor = RoseRed)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Hüküm ve koşulları okudum ve kabul ediyorum.")
    }
}

@Composable
fun DisclaimerDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Hüküm ve Koşullar") },
        text = {
            Text(
                "Bu uygulama, kullanıcılar arasında yük ve araç paylaşımına aracılık eder. " +
                        "Ancak uygulama yöneticileri, kullanıcılar arasında yapılan anlaşmalardan doğacak hukuki ve mali sorumlulukları kabul etmez. " +
                        "Kullanıcılar, kendi aralarındaki iletişim, ödeme ve diğer anlaşmaların sorumluluğunu tamamen kendileri taşır."
            )
        },
        confirmButton = {
            TextButton(onClick = { onAccept() }) {
                Text("Onaylıyorum", color = RoseRed)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Vazgeç", color = RoseRed)
            }
        }, containerColor = Color.White
    )
}