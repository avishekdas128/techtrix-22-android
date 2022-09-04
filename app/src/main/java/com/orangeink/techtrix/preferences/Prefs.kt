package com.orangeink.techtrix.preferences

import android.content.Context
import android.content.SharedPreferences
import com.orangeink.techtrix.home.data.model.HomeResponse
import com.orangeink.techtrix.login.data.model.Participant
import com.orangeink.techtrix.notifications.data.model.Notification
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Prefs(context: Context) {

    private val sharePref: String = "preferences"

    private val userPref: String = "user_pref"
    private val homePref: String = "home_pref"
    private val notificationPref: String = "notification_pref"
    private val trendingSearch: String = "trending_search"
    private val eventReminder: String = "event_reminder"
    private val announcements: String = "announcements"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(sharePref, Context.MODE_PRIVATE)

    private val moshi = Moshi.Builder().build()

    fun logout() {
        preferences.edit().clear().apply()
    }

    var user: Participant?
        get() = moshi.adapter(Participant::class.java)
            .fromJson(preferences.getString(userPref, "").toString())
        set(value) = preferences.edit()
            .putString(userPref, moshi.adapter(Participant::class.java).toJson(value)).apply()

    var home: HomeResponse?
        get() = moshi.adapter(HomeResponse::class.java)
            .fromJson(preferences.getString(homePref, "").toString())
        set(value) = preferences.edit()
            .putString(homePref, moshi.adapter(HomeResponse::class.java).toJson(value)).apply()

    var notifications: List<Notification>?
        get() = moshi.adapter<List<Notification>>(
            Types.newParameterizedType(
                List::class.java,
                Notification::class.java
            )
        ).fromJson(
            preferences.getString(notificationPref, "").toString()
        )
        set(value) = preferences.edit().putString(
            notificationPref, moshi.adapter<List<Notification>>(
                Types.newParameterizedType(
                    List::class.java,
                    Notification::class.java
                )
            ).toJson(value)
        ).apply()

    var trending: List<String>?
        get() = moshi.adapter<List<String>>(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            )
        ).fromJson(
            preferences.getString(trendingSearch, "").toString()
        )
        set(value) = preferences.edit().putString(
            trendingSearch, moshi.adapter<List<String>>(
                Types.newParameterizedType(
                    List::class.java,
                    String::class.java
                )
            ).toJson(value)
        ).apply()

    var notificationEventReminder: Boolean
        get() = preferences.getBoolean(eventReminder, true)
        set(value) = preferences.edit().putBoolean(eventReminder, value).apply()

    var notificationAnnouncements: Boolean
        get() = preferences.getBoolean(announcements, true)
        set(value) = preferences.edit().putBoolean(announcements, value).apply()
}