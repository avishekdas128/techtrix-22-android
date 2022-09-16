package com.orangeink.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "_id") val id: Int? = null,
    val name: String? = null,
    val category: String? = null,
    val desc: String? = null,
    val rules: String? = null,
    val contact: String? = null,
    val fee: Int? = null,
    val tags: List<String> = arrayListOf(),
    val popular: Boolean? = null,
    val flagship: Boolean? = null,
    val info: String? = null,
    val poster: String? = null,
    @Json(name = "regs_enabled") val regEnabled: Boolean? = null,
    @Json(name = "min_participants") val minParticipant: Int? = null,
    @Json(name = "max_participants") val maxParticipant: Int? = null,
)
