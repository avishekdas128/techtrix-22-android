package com.orangeink.notifications.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.NotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val service: NotificationService
) : BaseDataSource(), NotificationRepository {

    override suspend fun getNotifications() = getResult {
        service.getNotifications()
    }
}