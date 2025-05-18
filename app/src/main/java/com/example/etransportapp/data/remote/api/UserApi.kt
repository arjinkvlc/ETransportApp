package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.auth.LoginRequest
import com.example.etransportapp.data.model.auth.LoginResponse
import com.example.etransportapp.data.model.auth.RegisterRequest
import com.example.etransportapp.data.model.auth.RegisterResponse
import com.example.etransportapp.data.model.auth.ResendEmailConfirmCodeResponse
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

    @POST("ResendEmailConfirmCode")
    suspend fun resendConfirmationCode(@Query("email") email: String): Response<ResendEmailConfirmCodeResponse>

    @POST("Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}