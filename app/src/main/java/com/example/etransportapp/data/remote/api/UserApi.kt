package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.auth.BasicResponse
import com.example.etransportapp.data.model.auth.ConfirmEmailResponse
import com.example.etransportapp.data.model.auth.LoginRequest
import com.example.etransportapp.data.model.auth.LoginResponse
import com.example.etransportapp.data.model.auth.RegisterRequest
import com.example.etransportapp.data.model.auth.RegisterResponse
import com.example.etransportapp.data.model.auth.ResendEmailConfirmCodeResponse
import com.example.etransportapp.data.model.auth.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("Register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("ConfirmEmail")
    suspend fun confirmEmail(
        @Query("email") email: String,
        @Query("token") token: String
    ): Response<ConfirmEmailResponse>

    @POST("ResendEmailConfirmCode")
    suspend fun resendConfirmationCode(@Query("email") email: String): Response<ResendEmailConfirmCodeResponse>

    @POST("Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("GenerateForgotPasswordToken")
    suspend fun generateForgotPasswordToken(@Body emailBody: Map<String, String>): Response<BasicResponse>

    @POST("ForgotPassword")
    suspend fun forgotPassword(@Body request: Map<String, String>): Response<BasicResponse>

    @GET("{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<UserProfileResponse>



}