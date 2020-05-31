package com.harper.carnet.ui.session.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Place
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.harper.carnet.ui.map.delegate.NavigationMapDelegate
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
    private val mapDelegate: NavigationMapDelegate = MapDelegate { requireContext() }.withNavigation()
    private var currentLayoutState: LayoutState = LayoutState.DEFAULT

    private val searchHistoryAdapter: SearchHistoryAdapter = SearchHistoryAdapter { requireContext() }
    private val searchHintsAdapter: SearchHintsAdapter =
        SearchHintsAdapter({ requireContext() }) { onSearchHintClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentLayoutState = LayoutState.DEFAULT

        with(viewModel) {
            searchHintsLiveData.observe(this@SessionCreateFragment, ::setSearchHints)
            selectedPlaceLiveData.observe(this@SessionCreateFragment, ::applySearchResult)
            sessionCreatedLiveData.observe(this@SessionCreateFragment, ::onSessionCreation)
            currentLocationLiveData.observe(this@SessionCreateFragment) {
                if (mapDelegate.isMapReady)
                    mapDelegate.setOriginLocation(it)
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapDelegate.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapDelegate.onStart()
        viewModel.startLocationUpdates()
        destEditText.addTextChangedListener(onDestTextChangeListener)
    }

    override fun onResume() {
        super.onResume()
        mapDelegate.onResume()
    }

    override fun onStop() {
        destEditText.removeTextChangedListener(onDestTextChangeListener)
        viewModel.stopLocationUpdates()
        mapDelegate.onStop()
        super.onStop()
    }

    override fun onPause() {
        mapDelegate.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        mapDelegate.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroyView() {
        mapDelegate.onDestroy()
        super.onDestroyView()
    }

    private fun onSessionCreation(isCreated: Boolean) {
        if (isCreated) {
            Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
                .popBackStack()
        } else {
            //TODO Show message here
        }
    }

    private fun onSearchHintClicked(place: Place) {
        viewModel.onSearchItemClicked(place)
    }

    private fun applySearchResult(place: Place) {
        destEditText.apply {
            setText(place.name)
            clearFocus()
        }

        val currentLocation = viewModel.currentLocationLiveData.value ?: return
        if (mapDelegate.isMapReady) {
            mapDelegate.createRoute(currentLocation, place.location)
        } else {
            mapView.getMapAsync {
                mapDelegate.onMapReady(mapView, it)
                mapDelegate.createRoute(currentLocation, place.location)
            }
        }

        changeLayoutState(LayoutState.PLACE_SELECTED)
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

        val revealTarget = when (layoutState) {
            LayoutState.DEFAULT -> {
                toolbar.visibility = View.VISIBLE
                search_history_recycler
            }
            LayoutState.PLACE_SEARCHING -> {
                toolbar.visibility = View.GONE
                search_hints_recycler
            }
            LayoutState.PLACE_SELECTED -> {
                toolbar.visibility = View.VISIBLE
                session_container_create
            }
        }

        val hideTarget = container.children.find { it.visibility == View.VISIBLE }
        if (hideTarget != null) {
            hideTarget.animate()
                .withEndAction { hideTarget.visibility = View.GONE }
                .setDuration(400L)
                .alpha(0f)
                .start()
        }

        revealTarget.animate()
            .withStartAction { revealTarget.visibility = View.VISIBLE }
            .setDuration(400L)
            .alpha(1f)
            .start()
    }

    private val onDestTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val newLayoutState = if (s.isEmpty()) LayoutState.DEFAULT else LayoutState.PLACE_SEARCHING
            if (newLayoutState == LayoutState.PLACE_SEARCHING)
                viewModel.onSearchUpdated(s.toString())
            if (currentLayoutState != newLayoutState)
                changeLayoutState(newLayoutState)
        }
    }

    enum class LayoutState {
        DEFAULT, PLACE_SELECTED, PLACE_SEARCHING
    }
}