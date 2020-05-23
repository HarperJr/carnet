package com.harper.carnet.ui.session.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Notification
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.session.SessionsViewModel
import com.harper.carnet.ui.session.notifications.adapter.NotificationsAdapter
import kotlinx.android.synthetic.main.fragment_session_notifications.*
import org.koin.android.viewmodel.ext.android.getViewModel

class SessionNotificationsFragment : Fragment(R.layout.fragment_session_notifications) {
    private val viewModel: SessionsViewModel by lazy {
        parentFragment!!.getViewModel<SessionsViewModel>()
    }

    private val adapter: NotificationsAdapter = NotificationsAdapter { requireContext() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            activeSessionLiveData.observe(this@SessionNotificationsFragment) { setActiveSessionNotifications(it.notifications) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = this@SessionNotificationsFragment.adapter
        }
    }

    private fun setActiveSessionNotifications(notifications: List<Notification>) {
        adapter.submitList(notifications)
    }
}