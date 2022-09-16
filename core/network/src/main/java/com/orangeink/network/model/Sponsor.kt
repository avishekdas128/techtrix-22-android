package com.orangeink.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sponsor(
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var links: List<String> = arrayListOf()
)
