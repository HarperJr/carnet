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
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.ext.cast
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.map.MapFragment
import com.harper.carnet.ui.map.delegate.MapDelegate
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
    private val permissionsDelegate: PermissionsDelegate =
        PermissionsDelegate(this, 2048, Permission.COARSE_LOCATION, Permission.FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            activeSessionLiveData.observe(this@SessionsFragment, ::setActiveSession)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabLayout.setupWithViewPager(tabViewPager.also {
            it.adapter = TabPagerAdapter(requireContext(), childFragmentManager)
        })
        btnCreateSession.setOnClickListener { onCreateSessionBtnClicked() }
    }

    private fun onCreateSessionBtnClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.sessionCreateFragment)
    }

    override fun onDestroyView() {
        permissionsDelegate.onPermissionsListener = null
        tabLayout.clearOnTabSelectedListeners()
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
            bindMap(session)
            bindValues(session.values)

            valueWarnings.text = session.notifications.count().toString()
            valueRotates.text = "0"
        }
    }

    private fun bindValues(values: List<Value<*>>) {
        if (view != null) {
            for (value in values) {
                val viewId = resolveValueId(value)
                if (viewId != -1)
                    view!!.findViewById<TextView>(viewId).text = ValueFormatter.format(value)
            }
        }
    }

    private fun resolveValueId(value: Value<*>): Int {
        return VALUE_IDS[value.type] ?: -1
    }

    private fun bindMap(session: Session) {
        val start = session.startLocation.latLng
        val end = session.endLocation.latLng

        val latNorth = if (start.lat > end.lat) start.lat else end.lat
        val latSouth = if (start.lat < end.lat) start.lat else end.lat
        val lonEast = if (start.lng > end.lng) start.lng else end.lng
        val lonWest = if (start.lng < end.lng) start.lng else end.lng

        mapView.doOnPreDraw { imageView ->
            loadMapRegionIntoView(imageView.cast(), latNorth, latSouth, lonEast, lonWest)
        }
    }

    private fun loadMapRegionIntoView(
        imageView: ImageView,
        latNorth: Double,
        latSouth: Double,
        lonEast: Double,
        lonWest: Double
    ) {
        val options = MapSnapshotter.Options(imageView.width, imageView.height)
            .withRegion(LatLngBounds.from(latNorth, lonEast, latSouth, lonWest))
            .withStyleBuilder(Style.Builder().fromUri(MapDelegate.MAP_BOX_STYLE_CUSTOM))
            .withLogo(false)

        MapSnapshotter(requireContext(), options)
            .start { imageView.setImageBitmap(it.bitmap) }
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