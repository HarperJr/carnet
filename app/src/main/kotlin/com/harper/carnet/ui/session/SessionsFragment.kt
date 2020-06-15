package com.harper.carnet.ui.session

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.ext.cast
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.harper.carnet.ui.map.delegate.MapDrawManager
import com.harper.carnet.ui.map.delegate.NavigationMapDelegate
import com.harper.carnet.ui.session.adapter.TabPagerAdapter
import com.harper.carnet.ui.support.ValueFormatter
import com.harper.carnet.ui.support.perms.Permission
import com.harper.carnet.ui.support.perms.PermissionsDelegate
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter
import kotlinx.android.synthetic.main.fragment_sessions.*
import kotlinx.android.synthetic.main.include_session_values.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class SessionsFragment : Fragment(R.layout.fragment_sessions) {
    private val viewModel: SessionsViewModel by currentScope.viewModel(this)
    private val mapDelegate: NavigationMapDelegate = MapDelegate { requireContext() }.withNavigation()
    private val permissionsDelegate: PermissionsDelegate =
        PermissionsDelegate(this, 2048, Permission.COARSE_LOCATION, Permission.FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            activeSessionLiveData.observe(this@SessionsFragment, ::setActiveSession)
            activeSessionDiagnosticsProvider.observe(this@SessionsFragment, ::setDiagnosticValues)
        }

        mapDelegate.disableZoomBounds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabLayout.setupWithViewPager(tabViewPager.also {
            it.adapter = TabPagerAdapter(requireContext(), childFragmentManager)
        })
        btnCreateSession.setOnClickListener { onCreateSessionBtnClicked() }
        mapView.getMapAsync {
            mapDelegate.onMapReady(mapView, it)
        }
    }

    private fun onCreateSessionBtnClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.sessionCreateFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapDelegate.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapDelegate.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapDelegate.onResume()
    }

    override fun onPause() {
        mapDelegate.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapDelegate.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        mapDelegate.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroyView() {
        permissionsDelegate.onPermissionsListener = null
        tabLayout.clearOnTabSelectedListeners()
        mapDelegate.onDestroy()
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setActiveSession(session: Session) {
        if (session == Session.EMPTY) {
            changeLayoutState(LayoutState.CREATE_SESSION)
        } else {
            changeLayoutState(LayoutState.ACTIVE_SESSION)

            mapDelegate.createRoute(session.startLocation.latLng, session.endLocation.latLng)
            mapDelegate.setFocusInBounds(session.startLocation.latLng, session.endLocation.latLng)

            valueWarnings.text = session.notifications.count().toString()
            valueRotates.text = "0"
        }
    }

    private fun setDiagnosticValues(diagnosticValues: List<DiagnosticValue<*>>) {
        if (view != null) {
            for ((valueType, viewId) in VALUE_IDS) {
                val value = diagnosticValues.find { it.type == valueType }
                if (viewId != -1)
                    requireView().findViewById<TextView>(viewId).text =
                        value?.let { ValueFormatter.format(it) } ?: getString(R.string.diagnostics_stub)
            }
        }
    }

    private fun changeLayoutState(layoutState: LayoutState) {
        createSessionLayout.isVisible = layoutState == LayoutState.CREATE_SESSION
        activeSessionLayout.isVisible = layoutState == LayoutState.ACTIVE_SESSION

        val constraintSet = ConstraintSet().apply { clone(container) }
        when (layoutState) {
            LayoutState.CREATE_SESSION -> {
                constraintSet.connect(
                    R.id.tabsContainer,
                    ConstraintSet.TOP,
                    R.id.createSessionLayout,
                    ConstraintSet.BOTTOM
                )
            }
            LayoutState.ACTIVE_SESSION -> {
                constraintSet.connect(
                    R.id.tabsContainer,
                    ConstraintSet.TOP,
                    R.id.activeSessionLayout,
                    ConstraintSet.BOTTOM
                )
            }
        }

        constraintSet.applyTo(container)
    }

    enum class LayoutState {
        CREATE_SESSION, ACTIVE_SESSION
    }

    companion object {
        private val VALUE_IDS = mapOf(
            ValueType.VOLTAGE to R.id.valueAccumulator,
            ValueType.SPEED to R.id.valueSpeed,
            ValueType.FUEL_LEVEL to R.id.valueFuel
        )
    }
}