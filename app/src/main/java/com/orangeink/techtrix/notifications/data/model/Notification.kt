package com.orangeink.techtrix.notifications.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Notification(
    @Json(name = "_id") var Id: String? = null,
    var title: String? = null,
    var description: String? = null,
    @Json(name = "event_id") var eventId: String? = null,
    var type: String? = null
)
