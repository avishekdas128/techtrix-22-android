package com.orangeink.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS: Long = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEK_MILLIS = 7 * DAY_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS
private const val YEAR_MILLIS = 365 * DAY_MILLIS

fun formatDate(inputText: String): String {
    val input = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    )
    input.timeZone = TimeZone.getTimeZone("UTC")
    var d: Date? = null
    try {
        d = input.parse(inputText)
    } catch (e: Exception) {
        Timber.e(e)
    }
    var time: Long = 0
    if (d != null) {
        time = d.time
    }
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return ""
    }
    val diff = now - time
    return if (diff < MINUTE_MILLIS) {
        "just now"
    } else if (diff < 2 * MINUTE_MILLIS) {
        "1m"
    } else if (diff < 50 * MINUTE_MILLIS) {
        (diff / MINUTE_MILLIS).toString() + "m"
    } else if (diff < 90 * MINUTE_MILLIS) {
        "1h"
    } else if (diff < 24 * HOUR_MILLIS) {
        (diff / HOUR_MILLIS).toString() + "h"
    } else if (diff < 48 * HOUR_MILLIS) {
        "1d"
    } else if (diff < 7 * DAY_MILLIS) {
        (diff / DAY_MILLIS).toString() + "d"
    } else if (diff < 12 * DAY_MILLIS) {
        "1w"
    } else if (diff < 4 * WEEK_MILLIS) {
        if (diff / WEEK_MILLIS == 1L) {
            "1w"
        } else {
            (diff / WEEK_MILLIS).toString() + "w"
        }
    } else if (diff < 52 * WEEK_MILLIS) {
        if (diff / MONTH_MILLIS == 1L) {
            "1m"
        } else {
            (diff / MONTH_MILLIS).toString() + "m"
        }
    } else {
        if (diff / YEAR_MILLIS == 1L) {
            "1y"
        } else {
            (diff / YEAR_MILLIS).toString() + "y"
        }
    }
}