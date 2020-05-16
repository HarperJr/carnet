package com.harper.carnet.ui.session

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import com.harper.carnet.domain.model.Session
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.map.MapFragment
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.harper.carnet.ui.session.adapter.TabPagerAdapter
import com.harper.carnet.ui.support.perms.Permission
import com.harper.carnet.ui.support.perms.PermissionsDelegate
import kotlinx.android.synthetic.main.fragment_sessions.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class SessionsFragment : Fragment(R.layout.fragment_sessions) {
    private val viewModel: SessionsViewModel by currentScope.viewModel(this)
    private val mapDelegate: MapDelegate = MapDelegate { requireContext() }
    private val permissionsDelegate: PermissionsDelegate =
        PermissionsDelegate(this, Permission.COARSE_LOCATION, Permission.FINE_LOCATION)

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
            .navigate(R.id.mapFragment, MapFragment.createSessionArg())
    }

    override fun onStart() {
        super.onStart()
        mapDelegate.onStart()
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
//            permissionsDelegate.requestPermissions()
//            permissionsDelegate.onPermissionsListener = object : OnPermissionsListener {
//                override fun onGrantSuccess(permissions: List<Permission>) {
//                    if (permissions.contains(Permission.FINE_LOCATION))
//                        mapView.getMapAsync {
//                            mapDelegate.onMapReady(mapView, it)
//                        }
//                }
//
//                override fun onGrantFail(permissions: List<Permission>) {
//                    if (permissions.contains(Permission.FINE_LOCATION))
//                        permissionsDelegate.requestPermissions()
//                }
//            }
        }
    }

    private fun changeLayoutState(layoutState: LayoutState) {
        applyStateToConstraints(layoutState)
    }

    private fun applyStateToConstraints(layoutState: LayoutState) {
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
}