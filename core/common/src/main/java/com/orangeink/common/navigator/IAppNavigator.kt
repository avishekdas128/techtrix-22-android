package com.orangeink.common.navigator

import android.content.Context
import android.content.Intent
import com.orangeink.common.constants.ScreenType

interface IAppNavigator {

    fun provideCategoryActivity(context: Context, categoryId: String): Intent

    fun provideEventActivity(
        context: Context,
        eventId: Int,
        eventTitle: String? = null,
        eventCategory: String? = null
    ): Intent

    fun provideListActivity(
        context: Context,
        @ScreenType screenType: Int
    ): Intent
}