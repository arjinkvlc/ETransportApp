package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.auth.RegisterRequest
import com.example.etransportapp.data.model.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("Register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("ConfirmEmail")
    suspend fun confirmEmail(
        @Query("email") email: String,
        @Query("token") token: String
    ): Response<Unit>
}