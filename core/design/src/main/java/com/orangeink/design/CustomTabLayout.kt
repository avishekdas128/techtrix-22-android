package com.orangeink.design

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CustomTabLayout : TabLayout {

    /**
     * Constructor
     *
     * @param context Context
     */
    constructor(context: Context) : super(context)

    /**
     * Constructor
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     */
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    /**
     * Constructor
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     * @param defStyleAttr int
     */
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    /**
     * Initialize the process to get custom attributes from xml and set button params.
     */
    private fun init() {
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                setupTabs(tab, true)
            }

            override fun onTabUnselected(tab: Tab) {
                setupTabs(tab, false)
            }

            override fun onTabReselected(tab: Tab) {}
        })
    }

    private fun setupTabs(tab: Tab, isSelected: Boolean) {
        tab.customView?.let {
            it.findViewById<TextView>(R.id.tv_tab_label).background =
                ContextCompat.getDrawable(
                    context,
                    if (isSelected)
                        R.drawable.bg_custom_tab_selected
                    else
                        R.drawable.bg_custom_tab_unselected
                )
            it.findViewById<TextView>(R.id.tv_tab_label)
                .setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (isSelected) R.color.white else R.color.purple_400
                    )
                )
        }
    }

    fun setupMediator(viewPager: ViewPager2, pageTitle: (Int) -> String) {
        TabLayoutMediator(this, viewPager) { tab: Tab, position: Int ->
            tab.setCustomView(R.layout.layout_custom_tab)
            tab.customView?.let {
                (it.findViewById<View>(R.id.tv_tab_label) as TextView).text = pageTitle(position)
            }
        }.attach()
    }
}