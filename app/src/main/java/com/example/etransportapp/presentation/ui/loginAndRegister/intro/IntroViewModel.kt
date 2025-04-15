package com.example.etransportapp.presentation.ui.loginAndRegister.intro

import android.content.Context
import androidx.lifecycle.ViewModel

class IntroViewModel : ViewModel() {

    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_logged_in", false)
    }
}