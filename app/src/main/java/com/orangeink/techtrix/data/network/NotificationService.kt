package com.orangeink.techtrix.data.network

import com.orangeink.techtrix.notifications.data.model.Notification
import retrofit2.Response
import retrofit2.http.GET

interface NotificationService {

    companion object {
        const val NOTIFICATION_ENDPOINT = "https://techtrix22-fcm.herokuapp.com"
    }

    //Notifications
    @GET("/")
    suspend fun getNotifications(): Response<List<Notification>>

}