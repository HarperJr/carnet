package com.harper.carnet.ui.session.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Place
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.session.create.adapter.SearchHintsAdapter
import com.harper.carnet.ui.session.create.adapter.SearchHistoryAdapter
import kotlinx.android.synthetic.main.fragment_session_create.*
import kotlinx.android.synthetic.main.include_session_create_default.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

/**
 * Created by HarperJr on 16:24
 **/
class SessionCreateFragment : Fragment(R.layout.fragment_session_create) {
    private val viewModel: SessionCreateViewModel by currentScope.viewModel(this)
    private var currentLayoutState: LayoutState = LayoutState.DEFAULT

    private val searchHistoryAdapter: SearchHistoryAdapter = SearchHistoryAdapter { requireContext() }
    private val searchHintsAdapter: SearchHintsAdapter =
        SearchHintsAdapter({ viewModel.onSearchItemClicked(it) }) { requireContext() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            searchHintsLiveData.observe(this@SessionCreateFragment, ::setSearchHints)
            searchHintLiveData.observe(this@SessionCreateFragment, ::applySearchResult)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener { onNavigationBtnClicked() }
        btnCreateSession.setOnClickListener { viewModel.onCreateBtnClicked() }
        search_history_recycler.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = searchHistoryAdapter
        }

        search_hints_recycler.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = searchHintsAdapter
        }

        changeLayoutState(currentLayoutState)
    }

    override fun onStart() {
        super.onStart()
        destEditText.addTextChangedListener(onDestTextChangeListener)
        viewModel.startLocationUpdates()
    }

    override fun onStop() {
        destEditText.removeTextChangedListener(onDestTextChangeListener)
        viewModel.stopLocationUpdates()
        super.onStop()
    }

    private fun applySearchResult(result: Place) {
        destEditText.apply {
            setText(result.place)
            clearFocus()
        }

        changeLayoutState(LayoutState.DEFAULT)
    }

    private fun onNavigationBtnClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .popBackStack()
    }

    private fun setSearchHints(hints: List<Place>) {
        searchHintsAdapter.submitList(hints)
    }

    private fun changeLayoutState(layoutState: LayoutState) {
        currentLayoutState = layoutState

        val targets = if (layoutState == LayoutState.DEFAULT) {
            toolbar.visibility = View.VISIBLE
            session_container_default to search_hints_recycler
        } else {
            toolbar.visibility = View.GONE
            search_hints_recycler to session_container_default
        }
        val (revealTarget, hideTarget) = targets

        revealTarget.animate()
            .withStartAction { revealTarget.visibility = View.VISIBLE }
            .setDuration(250L)
            .alpha(1f)
            .start()

        hideTarget.animate()
            .withEndAction { hideTarget.visibility = View.GONE }
            .setDuration(250L)
            .alpha(0f)
            .start()
    }

    private val onDestTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val newLayoutState = if (s.isEmpty()) LayoutState.DEFAULT else LayoutState.SEARCH_HINTS
            if (newLayoutState == LayoutState.SEARCH_HINTS)
                viewModel.onSearchUpdated(s.toString())
            if (currentLayoutState != newLayoutState)
                changeLayoutState(newLayoutState)
        }
    }

    enum class LayoutState {
        DEFAULT, SEARCH_HINTS
    }
}