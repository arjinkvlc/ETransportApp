package com.example.etransportapp.presentation.ui.loginAndRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.etransportapp.R
import com.example.etransportapp.presentation.ui.loginAndRegister.intro.IntroScreen

class LoginAndRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntroScreen()
        }
    }
}