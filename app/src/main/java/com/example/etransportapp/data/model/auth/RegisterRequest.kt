package com.example.etransportapp.data.model.auth

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val birthYear: Int = 2000,
    val email: String,
    val phoneNumber: String,
    val userName: String,
    val password: String,
    val confirmPassword: String
)
