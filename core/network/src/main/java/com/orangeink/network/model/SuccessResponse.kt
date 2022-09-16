package com.orangeink.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuccessResponse(
    var success: Boolean? = null
)
