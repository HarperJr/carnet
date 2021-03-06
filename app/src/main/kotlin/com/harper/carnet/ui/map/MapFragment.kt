package com.harper.carnet.ui.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.harper.carnet.R
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.ext.cast
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
    private val telematicsDisplayManager: TelematicsDisplayManager = TelematicsDisplayManager(mapDelegate)
    private val bottomSheetBehavior by lazy {
        notificationBottomSheet.layoutParams
            .cast<CoordinatorLayout.LayoutParams>().behavior!!.cast<BottomSheetBehavior<*>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapDelegate.onMapMoveListener = {
            mapDelegate.setIsTracking(false)
            btnOrigin.isActivated = false
        }
        with(viewModel) {
            locationsLiveData.observe(this@MapFragment) {
                if (!mapDelegate.isNavigating)
                    mapDelegate.setOriginLocation(it)
            }
            telematicsLiveData.observe(this@MapFragment, ::setTelematics)
            activeSessionLiveData.observe(this@MapFragment) {
                if (it == Session.EMPTY) return@observe
                if (!mapDelegate.isRoutingRunning)
                    mapDelegate.createRoute(it.startLocation.latLng, it.endLocation.latLng)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapDelegate.onCreate(savedInstanceState)

        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isFitToContents = true
        permissionsDelegate.onPermissionsListener = onPermissionsListener
        btnOrigin.setOnClickListener {
            mapDelegate.setIsTracking(true)
            btnOrigin.isActivated = true
        }

        btnOrigin.isActivated = true
        mapDelegate.setIsTracking(true)

        btnNewNotification.setOnClickListener { onNewNotificationBtnClicked() }
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        mapDelegate.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapDelegate.onStart()
        permissionsDelegate.requestPermissions()
        bindTelematicsService()
    }

    override fun onStop() {
        mapDelegate.onStop()
        unbindTelematicsService()
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

    private fun bindTelematicsService() {
        requireActivity().bindService(
            Intent(requireContext(), TelematicsService::class.java),
            viewModel.getServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindTelematicsService() {
        requireActivity().unbindService(viewModel.getServiceConnection())
    }

    private fun onNewNotificationBtnClicked() {
        setNotificationBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
    }

    private fun setNotificationBottomSheetState(state: Int) {
        bottomSheetBehavior.state = state
    }

    private fun setTelematics(telematics: List<Telematics>) {
        telematicsDisplayManager.setTelematics(telematics)
    }

    private fun prepareMap() {
        mapView.getMapAsync {
            mapDelegate.onMapReady(mapView, it)
        }
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