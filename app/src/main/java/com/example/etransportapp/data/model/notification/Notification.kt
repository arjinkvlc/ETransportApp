package com.example.etransportapp.data.model.notification

data class Notification(
    val createdDate: String,
    val id: Int,
    val isRead: Boolean,
    val message: String,
    val relatedEntityId: Int,
    val title: String,
    val type: Int
)