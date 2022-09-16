package com.orangeink.network.service

import com.orangeink.network.model.Notification
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