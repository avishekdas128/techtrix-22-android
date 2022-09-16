package com.orangeink.common.constants

import androidx.annotation.StringDef

@StringDef(
    Identifier.CATEGORY_ID,
    Identifier.EVENT_ID,
    Identifier.SCREEN_TYPE,
    Identifier.EVENT_TITLE,
    Identifier.EVENT_CATEGORY
)
@Retention(AnnotationRetention.SOURCE)
annotation class Identifier {
    companion object {
        const val CATEGORY_ID = "category_id"
        const val EVENT_ID = "event_id"
        const val EVENT_TITLE = "event_title"
        const val EVENT_CATEGORY = "event_category"
        const val SCREEN_TYPE = "screen_type"
    }
}