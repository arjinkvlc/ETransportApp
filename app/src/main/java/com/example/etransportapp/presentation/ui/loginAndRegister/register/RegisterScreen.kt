package com.example.etransportapp.presentation.ui.loginAndRegister.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.R
import com.example.etransportapp.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToNext: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(scrollState),  // Sayfanın kaydırılabilir olmasını sağlıyor
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ Firma İsmi ve Logo
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "App Logo",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.FillHeight
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.e_tasimacilik),
                contentDescription = "App text",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Kayıt Ol",
            fontSize = 24.sp,
            color = LightBlue, fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = viewModel.companyName,
            onValueChange = { viewModel.companyName = it },
            label = { Text("Firma Adı") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),// ✅ Daha yuvarlak köşeler
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("Ad") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Soyad") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("E-posta") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.phoneNumber = it },
            label = { Text("Telefon Numarası") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Şifre") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "👁️" else "🔒") // Şifre gösterme/gizleme ikonu
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = { Text("Şifreyi Onayla") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Text(if (confirmPasswordVisible) "👁️" else "🔒")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, // **Tıklanınca beyaz kalmasını sağlar**
                unfocusedContainerColor = Color.White, // **Normalde de beyaz**
                cursorColor = LightBlue, // **Yazı yazarken imleç rengi**
                focusedIndicatorColor = LightBlue, // **Seçili kenarlık rengi**
                unfocusedIndicatorColor = Color.Gray, // **Boşken kenarlık rengi**f
                focusedLabelColor = LightBlue, // **✅ Label (Firma Adı) seçiliyken LightBlue olur
                unfocusedLabelColor = Color.Gray // **Boşken gri olur**
            )
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End // ✅ Butonu sağa hizalar
        ) {
            Button(
                onClick = {
                    viewModel.registerUser(context, onNavigateToNext)
                },
                modifier = Modifier
                    .width(120.dp) // ✅ Butonun boyutunu küçülttük
                    .height(40.dp), // ✅ Daha küçük yükseklik
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(12.dp) // ✅ Köşeleri biraz daha yumuşattık
            ) {
                Text("İlerle", fontSize = 14.sp) // ✅ Daha küçük yazı boyutu
            }
        }


        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(onNavigateToNext = {})
}
