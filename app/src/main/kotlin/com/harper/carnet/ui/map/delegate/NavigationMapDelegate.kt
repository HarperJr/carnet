package com.harper.carnet.ui.map.delegate

import android.content.Context
import android.location.Location
import com.harper.carnet.BuildConfig
import com.harper.carnet.R
import com.harper.carnet.domain.map.route.RouteCallback
import com.harper.carnet.domain.map.route.RouteProvider
import com.harper.carnet.domain.model.LatLng
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.LineString
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine
import com.mapbox.services.android.navigation.v5.navigation.*
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress
import timber.log.Timber


class NavigationMapDelegate(contextProvider: () -> Context) : MapDelegate(contextProvider), NavigationEventListener,
    OffRouteListener, ProgressChangeListener {
    var isRoutingRunning: Boolean = false
        private set

    private var navigation: MapboxNavigation? = null
    // For testing
    private val mockLocationEngine: ReplayRouteLocationEngine = ReplayRouteLocationEngine()

    private val routeRefresh: RouteRefresh = RouteRefresh(BuildConfig.MAPBOX_TOKEN)
    private var wasInTunnel: Boolean = false

    override fun onMapReady(mapView: MapView, map: MapboxMap) {
        super.onMapReady(mapView, map)
        val options = MapboxNavigationOptions.builder()
            .isDebugLoggingEnabled(true)
            .build()
        navigation = MapboxNavigation(context, BuildConfig.MAPBOX_TOKEN, options)
            .apply {
                addNavigationEventListener(this@NavigationMapDelegate)
                locationEngine = mockLocationEngine
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigation?.onDestroy()

        navigation?.removeNavigationEventListener(this)
        navigation?.removeProgressChangeListener(this)
        navigation?.removeOffRouteListener(this)
        navigation = null
    }


    override fun onRunning(running: Boolean) {
        isRoutingRunning = running
        if (isRoutingRunning) {
            navigation?.addOffRouteListener(this)
            navigation?.addProgressChangeListener(this)
        }
    }

    override fun userOffRoute(location: Location?) {

    }

    override fun onProgressChange(location: Location, routeProgress: RouteProgress) {
        val isInTunnel = routeProgress.inTunnel()
        if (!wasInTunnel && isInTunnel)
            wasInTunnel = true

        if (wasInTunnel && !isInTunnel)
            wasInTunnel = false

        if (isTracking) {
            routeRefresh.refresh(routeProgress, refreshCallback)
            setOriginLocation(LatLng(location.latitude, location.longitude))
        }
    }

    fun createRoute(origin: LatLng, dest: LatLng) {
        RouteProvider.provide(context, origin, dest, object : RouteCallback {
            override fun onSuccess(route: DirectionsRoute) {
                resetNavigationRoute(route)
            }

            override fun onFail(throwable: Throwable) {
                Timber.e(throwable)
            }
        })

    }

    private fun resetLocationEngine(directionsRoute: DirectionsRoute) {
        mockLocationEngine.assign(directionsRoute)
    }

    private fun drawRouteOnMap(route: DirectionsRoute) {
        val routeCoords = LineString.fromPolyline(route.geometry()!!, Constants.PRECISION_6)
            .coordinates()
        if (routeCoords.isNotEmpty())
            mapDrawManager?.drawLine(ROUTE_LAYER, routeCoords, R.color.colorAccent, 0.8f, 4.5f)
    }

    private fun resetNavigationRoute(route: DirectionsRoute) {
        navigation?.startNavigation(route)
        resetLocationEngine(route)
        drawRouteOnMap(route)
    }

    private val refreshCallback: RefreshCallback = object : RefreshCallback {
        override fun onRefresh(route: DirectionsRoute) {
            resetNavigationRoute(route)
        }

        override fun onError(error: RefreshError) {
            Timber.e(error.message)
        }
    }

    companion object {
        private const val ROUTE_LAYER = "route_layer"
    }
}