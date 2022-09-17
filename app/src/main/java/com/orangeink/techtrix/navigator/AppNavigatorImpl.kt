package com.orangeink.techtrix.navigator

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.orangeink.category.CategoryActivity
import com.orangeink.common.constants.Identifier
import com.orangeink.common.constants.ScreenType
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.list.ListActivity
import com.orangeink.registration.ui.bottomsheet.RegisterBottomSheet
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
    ): Intent = Intent(context, com.orangeink.event.EventActivity::class.java).apply {
        putExtra(Identifier.EVENT_ID, eventId)
        eventTitle?.let { putExtra(Identifier.EVENT_TITLE, it) }
        eventCategory?.let { putExtra(Identifier.EVENT_CATEGORY, it) }
    }

    override fun provideListActivity(context: Context, @ScreenType screenType: Int): Intent =
        Intent(context, ListActivity::class.java).apply {
            putExtra(Identifier.SCREEN_TYPE, screenType)
        }

    override fun provideRegisterBottomSheet(
        fragmentManager: FragmentManager,
        eventId: Int,
        minParticipant: Int,
        maxParticipant: Int
    ) {
        val bottomSheet = RegisterBottomSheet.newInstance(eventId, minParticipant, maxParticipant)
        bottomSheet.show(fragmentManager, RegisterBottomSheet.TAG)
    }
}