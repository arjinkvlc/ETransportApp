package com.example.etransportapp.data.remote.service

import com.example.etransportapp.data.model.auth.RegisterRequest
import com.example.etransportapp.data.remote.api.UserApi
import retrofit2.Retrofit

class UserService(private val api: UserApi) {
    suspend fun registerUser(request: RegisterRequest) = api.register(request)
}
