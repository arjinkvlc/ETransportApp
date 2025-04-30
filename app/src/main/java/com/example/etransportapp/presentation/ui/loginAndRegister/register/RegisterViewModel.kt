package com.example.etransportapp.presentation.ui.loginAndRegister.register

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var companyName by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var selectedRole by mutableStateOf("")

    /**
     * ✅ Kullanıcı Kaydını Gerçekleştir
     * Bu fonksiyon backend API'ye istek atarak kullanıcıyı kaydeder.
     */
    fun registerUser(context: Context, onSuccess: () -> Unit) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(context, "Şifre en az 6 karakter olmalıdır", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ API Çağrısı
        CoroutineScope(Dispatchers.IO).launch {
            try {
              

                // Başarıyla kayıt olduktan sonra
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Kayıt başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
