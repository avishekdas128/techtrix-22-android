package com.orangeink.techtrix.search.data.model

import com.orangeink.techtrix.event.data.model.Event
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "events") val searchResult: List<Event>? = emptyList()
)