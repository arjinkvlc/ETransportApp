package com.example.etransportapp.data.model.auth

data class LoginResponse(
    val userId: String,
    val jwToken: String,
    val refreshToken: String,
    val emailValid: Boolean
)

