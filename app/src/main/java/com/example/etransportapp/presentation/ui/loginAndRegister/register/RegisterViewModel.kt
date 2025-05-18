package com.example.etransportapp.presentation.ui.loginAndRegister.register

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.auth.RegisterRequest
import com.example.etransportapp.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var birthYear by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var verificationCode by mutableStateOf("")

    var registeredUserId: String? = null

    fun registerUser(context: Context, onRegistered: () -> Unit) {
        val birthYearInt = birthYear.toIntOrNull() ?: 0

        val request = RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            birthYear = birthYearInt,
            email = email,
            phoneNumber = phoneNumber,
            userName = userName,
            password = password,
            confirmPassword = confirmPassword
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.userApi.register(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    registeredUserId = body?.userId // ✅ response'dan gelen userId
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Kayıt başarılı, mailinizi kontrol edin!", Toast.LENGTH_SHORT).show()
                        onRegistered()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Kayıt başarısız!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun confirmEmail(email: String, token: String, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.userApi.confirmEmail(email, token)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Email doğrulandı", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Kod yanlış veya süresi dolmuş", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Doğrulama Hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}