package com.orangeink.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Participant(
    var id: String? = null,
    var _id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var phone: Long? = null,
    @Json(name = "alt_phone") var altPhone: Long? = null,
    var institution: String? = null,
    var gender: String? = null,
    @Json(name = "general_fees") var generalFees: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class UpdateParticipant(
    var name: String? = null,
    var phone: Long? = null,
    @Json(name = "alt_phone") var altPhone: Long? = null,
    var institution: String? = null
)