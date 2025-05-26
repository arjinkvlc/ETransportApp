package com.example.etransportapp.data.remote.api

import com.example.etransportapp.data.model.notification.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApi {

    @GET("api/Notification")
    suspend fun getUserNotifications(): Response<List<Notification>>

    @GET("api/Notification/unread-count")
    suspend fun getUnreadNotificationCount(): Response<UnreadCountResponse>

    @PUT("api/Notification/{id}/mark-as-read")
    suspend fun markAsRead(@Path("id") id: Int): Response<Unit>

    @PUT("api/Notification/mark-all-as-read")
    suspend fun markAllAsRead(): Response<Unit>
}

data class UnreadCountResponse(val count: Int)
