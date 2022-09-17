package com.orangeink.notifications.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Resource<List<Notification>>
}