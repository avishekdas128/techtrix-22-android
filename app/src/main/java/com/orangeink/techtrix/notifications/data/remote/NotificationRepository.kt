package com.orangeink.techtrix.notifications.data.remote

import com.orangeink.techtrix.data.network.NotificationService
import com.orangeink.techtrix.util.BaseDataSource
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