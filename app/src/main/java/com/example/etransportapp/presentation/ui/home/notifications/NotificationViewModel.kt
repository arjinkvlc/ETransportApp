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

    fun fetchNotifications() {
        viewModelScope.launch {
            // Şimdilik dummy veri (backend bağlanınca gerçek API'den çekilecek)
            _notifications.value = listOf(
                Notification(
                    id = 1,
                    title = "Yeni Teklif",
                    message = "Fatih Kargo için bir teklif aldınız.",
                    createdDate = "2025-05-25",
                    isRead = false,
                    relatedEntityId = 101,
                    type = 1
                ),
                Notification(
                    id = 2,
                    title = "İlanınız Yayında",
                    message = "Yeni yük ilanınız başarıyla yayınlandı.",
                    createdDate = "2025-05-24",
                    isRead = true,
                    relatedEntityId = 102,
                    type = 2
                )
            )
        }
    }
}
