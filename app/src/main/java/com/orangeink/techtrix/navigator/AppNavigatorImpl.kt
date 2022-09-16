package com.orangeink.techtrix.navigator

import android.content.Context
import android.content.Intent
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.constants.Identifier
import com.orangeink.common.constants.ScreenType
import com.orangeink.techtrix.category.ui.CategoryActivity
import com.orangeink.techtrix.event.ui.EventActivity
import com.orangeink.techtrix.misc.ui.ListActivity
import dagger.Reusable
import javax.inject.Inject

@Reusable
class AppNavigatorImpl @Inject constructor() : IAppNavigator {

    override fun provideCategoryActivity(context: Context, categoryId: String): Intent =
        Intent(context, CategoryActivity::class.java).apply {
            putExtra(Identifier.CATEGORY_ID, categoryId)
        }

    override fun provideEventActivity(
        context: Context,
        eventId: Int,
        eventTitle: String?,
        eventCategory: String?
    ): Intent = Intent(context, EventActivity::class.java).apply {
        putExtra(Identifier.EVENT_ID, eventId)
        eventTitle?.let { putExtra(Identifier.EVENT_TITLE, it) }
        eventCategory?.let { putExtra(Identifier.EVENT_CATEGORY, it) }
    }

    override fun provideListActivity(context: Context, @ScreenType screenType: Int): Intent =
        Intent(context, ListActivity::class.java).apply {
            putExtra(Identifier.SCREEN_TYPE, screenType)
        }
}