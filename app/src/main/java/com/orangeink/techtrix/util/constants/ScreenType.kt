package com.orangeink.techtrix.util.constants

import androidx.annotation.IntDef

@IntDef(ScreenType.VIEW_SPONSOR, ScreenType.VIEW_TEAM, ScreenType.VIEW_ALL_EVENTS)
@Retention(AnnotationRetention.SOURCE)
annotation class ScreenType {
    companion object {
        const val VIEW_SPONSOR = 1
        const val VIEW_TEAM = 2
        const val VIEW_ALL_EVENTS = 3
    }
}