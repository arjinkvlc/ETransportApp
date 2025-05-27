package com.example.etransportapp.presentation.ui.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etransportapp.data.model.notification.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.notificationApi.getUserNotifications()
                if (response.isSuccessful) {
                    _notifications.value = (response.body() ?: emptyList()).filter { !it.isRead }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchUnreadCount() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.notificationApi.getUnreadNotificationCount()
                if (response.isSuccessful) {
                    _unreadCount.value = response.body()?.count ?: 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.notificationApi.markAsRead(notificationId)
                if (response.isSuccessful) {
                    _notifications.value = _notifications.value.map {
                        if (it.id == notificationId) it.copy(isRead = true) else it
                    }
                    fetchUnreadCount()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.notificationApi.markAllAsRead()
                if (response.isSuccessful) {
                    fetchNotifications()
                    fetchUnreadCount()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
