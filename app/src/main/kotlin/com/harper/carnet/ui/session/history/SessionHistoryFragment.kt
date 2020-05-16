package com.harper.carnet.ui.session.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.domain.model.Session
import com.harper.carnet.ui.session.SessionsViewModel
import com.harper.carnet.ui.session.history.adapter.SessionsAdapter
import kotlinx.android.synthetic.main.fragment_session_history.*
import org.koin.android.viewmodel.ext.android.getViewModel

class SessionHistoryFragment : Fragment(R.layout.fragment_session_history) {
    private val viewModel: SessionsViewModel by lazy {
        parentFragment!!.getViewModel<SessionsViewModel>()
    }

    private val adapter: SessionsAdapter = SessionsAdapter { requireContext() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            sessionsHistoryLiveData.observe({ lifecycle }, ::setSessionsHistory)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.adapter = adapter
    }

    private fun setSessionsHistory(sessions: List<Session>) {
        adapter.submitList(sessions)
    }
}