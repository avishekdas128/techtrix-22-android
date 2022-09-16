package com.orangeink.techtrix.notifications.data.remote

import com.orangeink.network.service.NotificationService
import com.orangeink.network.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val service: NotificationService
) : BaseDataSource() {

    suspend fun getNotifications() = getResult {
        service.getNotifications()
    }
}