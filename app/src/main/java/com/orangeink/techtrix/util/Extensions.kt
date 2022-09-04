package com.orangeink.techtrix.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.orangeink.techtrix.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

private const val SECOND_MILLIS: Long = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEK_MILLIS = 7 * DAY_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS
private const val YEAR_MILLIS = 365 * DAY_MILLIS

fun TextView.setData(text: String?) {
    if (text.isNullOrBlank())
        this.visibility = View.GONE
    else {
        this.text = text
        this.visibility = View.VISIBLE
    }
}

fun ImageView.loadImage(url: String) {
    GlideApp.with(this.context)
        .load(url)
        .error(R.drawable.no_image)
        .into(this)
}

fun ImageView.loadImage(uri: Uri) {
    GlideApp.with(this.context)
        .load(uri)
        .placeholder(R.drawable.dummy_avatar)
        .error(R.drawable.dummy_avatar)
        .into(this)
}

fun Context.pxToDp(value: Float): Int {
    val r: Resources = resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, r.displayMetrics
    ).roundToInt()
}

fun Context.setupTabView(isSelected: Boolean, tab: TabLayout.Tab) {
    tab.customView?.let {
        it.findViewById<View>(R.id.tv_tab_label).background =
            ContextCompat.getDrawable(
                this,
                if (isSelected)
                    R.drawable.bg_custom_tab_selected
                else
                    R.drawable.bg_custom_tab_unselected
            )
        (it.findViewById<View>(R.id.tv_tab_label) as TextView)
            .setTextColor(
                ContextCompat.getColor(
                    this,
                    if (isSelected) R.color.white else R.color.purple_400
                )
            )
    }
}

fun String.findIcon(context: Context): Drawable? {
    val drawableId = when (this.lowercase()) {
        "automata" -> R.drawable.ic_baseline_code_24
        "gaming" -> R.drawable.ic_outline_sports_esports_24
        "flagship" -> R.drawable.ic_round_outlined_flag_24
        "robotics" -> R.drawable.ic_robot
        "out of the box" -> R.drawable.ic_box
        "art facts" -> R.drawable.ic_round_color_lens_24
        else -> R.drawable.ic_outline_sports_esports_24
    }
    return ContextCompat.getDrawable(context, drawableId)
}

fun Context.openPlayStore() {
    val appPackageName = packageName
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (error: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() = Pattern.compile("^[6-9]\\d{9}\$").matcher(this).matches()

fun String.formatDate(): String {
    val input = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    )
    input.timeZone = TimeZone.getTimeZone("UTC")
    var d: Date? = null
    try {
        d = input.parse(this)
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