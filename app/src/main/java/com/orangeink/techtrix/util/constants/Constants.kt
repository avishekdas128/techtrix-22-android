package com.orangeink.techtrix.util.constants

import androidx.annotation.IntDef

@IntDef(Constants.VIEW_LIST, Constants.VIEW_GRID)
@Retention(AnnotationRetention.SOURCE)
annotation class Constants {
    companion object{
        const val VIEW_LIST = 1
        const val VIEW_GRID = 2
    }
}