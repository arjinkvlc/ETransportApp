package com.example.etransportapp.presentation.ui.loginAndRegister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.etransportapp.presentation.navigation.NavGraph
import com.example.etransportapp.ui.theme.ETransportAppTheme

class LoginAndRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ETransportAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                NavGraph(navController,Modifier,context)
            }
        }
    }
}
