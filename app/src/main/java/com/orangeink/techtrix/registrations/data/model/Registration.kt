package com.orangeink.techtrix.registrations.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Registration(
    var event: String? = null,
    var participants: ArrayList<String> = arrayListOf(),
    var paid: Boolean? = null,
    @Json(name = "team_name") var teamName: String? = null,
    @Json(name = "event_name") var eventName: String? = null,
    @Json(name = "event_category") var eventCategory: String? = null
)
