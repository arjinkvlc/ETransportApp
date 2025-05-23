package com.example.etransportapp.presentation.ui.loginAndRegister.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.auth.LoginRequest
import com.example.etransportapp.data.remote.RetrofitInstance
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(context: Context, onSuccess: () -> Unit) {
        val request = LoginRequest(email.trim(), password.trim())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.userApi.login(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.emailValid == true) {
                        PreferenceHelper.setLoggedIn(context, true)

                        //Saving jwt token and userId to Preference Helper
                        PreferenceHelper.saveJwtToken(context, body.jwToken)
                        PreferenceHelper.saveUserId(context, body.userId)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Email doğrulanmamış", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Giriş başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

