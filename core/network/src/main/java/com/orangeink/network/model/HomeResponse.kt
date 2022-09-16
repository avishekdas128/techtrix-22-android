package com.orangeink.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeResponse(
    val popular: List<Event> = arrayListOf(),
    val flagship: List<Event> = arrayListOf(),
    val categories: List<String> = arrayListOf(),
    val trending: List<String> = arrayListOf(),
    @Json(name = "update_required") val updateRequired: Boolean = false
)
