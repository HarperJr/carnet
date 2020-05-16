package com.harper.carnet.ui.session.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.harper.carnet.R
import com.harper.carnet.ui.session.history.SessionHistoryFragment
import com.harper.carnet.ui.session.notifications.SessionNotificationsFragment

class TabPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val containers: List<Pair<Fragment, Int>> = listOf(
        SessionHistoryFragment() to R.string.tab_history,
        SessionNotificationsFragment() to R.string.tab_notifications
    )

    override fun getItem(position: Int): Fragment {
        return containers[position].first
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(containers[position].second)
    }

    override fun getCount(): Int = containers.count()
}