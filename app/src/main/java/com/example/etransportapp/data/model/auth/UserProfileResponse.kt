package com.example.etransportapp.data.model.auth

data class UserProfileResponse(
    val birthYear: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val surname: String
)