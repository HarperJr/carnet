package com.harper.carnet.ui.map

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.harper.carnet.ui.map.delegate.NavigationMapDelegate
import com.harper.carnet.ui.support.perms.OnPermissionsListener
import com.harper.carnet.ui.support.perms.Permission
import com.harper.carnet.ui.support.perms.PermissionsDelegate
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel


class MapFragment : Fragment(R.layout.fragment_map) {
    private val viewModel: MapViewModel by currentScope.viewModel(this)
    private val mapDelegate: NavigationMapDelegate = MapDelegate { requireContext() }.withNavigation()
    private val permissionsDelegate: PermissionsDelegate =
        PermissionsDelegate(this, 4084, Permission.COARSE_LOCATION, Permission.FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapDelegate.onMapReadyListener = viewModel::onMapReady
        mapDelegate.onMapMoveListener = viewModel::onMapMoved
        with(viewModel) {
            locationsLiveData.observe(this@MapFragment) {
                if (!mapDelegate.isRoutingRunning)
                    mapDelegate.createRoute(it, LatLng(56.139670, 40.397905))
                mapDelegate.setOriginLocation(it)
            }
            originBtnActiveStateLiveData.observe(this@MapFragment, ::setTrackingState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapDelegate.onCreate(savedInstanceState)

        permissionsDelegate.onPermissionsListener = onPermissionsListener
        btnOrigin.setOnClickListener { viewModel.onUserOriginBtnClicked() }
        btnNewSession.setOnClickListener { onSessionBtnClicked() }
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        mapDelegate.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapDelegate.onStart()
        permissionsDelegate.requestPermissions()
    }

    override fun onStop() {
        mapDelegate.onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapDelegate.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapDelegate.onLowMemory()
    }

    override fun onDestroyView() {
        mapDelegate.onDestroy()
        permissionsDelegate.onPermissionsListener = null
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onSessionBtnClicked() {

    }

    private fun prepareMap() {
        mapView.getMapAsync {
            mapDelegate.onMapReady(mapView, it)
        }
    }

    private fun setTrackingState(isTracking: Boolean) {
        mapDelegate.setIsTracking(isTracking)
        btnOrigin.isActivated = isTracking
    }

    private val onPermissionsListener = object : OnPermissionsListener {
        override fun onGrantSuccess(permissions: List<Permission>) {
            if (permissions.contains(Permission.FINE_LOCATION))
                prepareMap()
        }

        override fun onGrantFail(permissions: List<Permission>) {
            if (permissions.contains(Permission.FINE_LOCATION)) {
                permissionsDelegate.requestPermissions()
            }
        }
    }
}