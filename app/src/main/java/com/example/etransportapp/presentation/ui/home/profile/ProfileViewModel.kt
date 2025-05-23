package com.example.etransportapp.presentation.ui.home.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.auth.UserProfileResponse
import com.example.etransportapp.data.remote.RetrofitInstance
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile = _userProfile.asStateFlow()

    fun fetchUserProfile(context: Context) {
        val userId = PreferenceHelper.getUserId(context) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.getUserApi(context).getUserProfile(userId)
            if (response.isSuccessful) {
                _userProfile.value = response.body()
            }
        }
    }
}
