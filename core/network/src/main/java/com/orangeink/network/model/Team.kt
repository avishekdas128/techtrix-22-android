package com.orangeink.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Team(
    var name: String? = null,
    var role: String? = null,
    var image: String? = null,
    var links: List<String> = arrayListOf(),
    @Json(name = "contact_phone") var phone: String? = null,
)