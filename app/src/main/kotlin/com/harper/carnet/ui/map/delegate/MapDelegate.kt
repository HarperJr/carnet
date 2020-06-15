package com.harper.carnet.ui.map.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.ui.support.Constants.MAX_ZOOM
import com.harper.carnet.ui.support.Constants.MIN_ZOOM
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager

open class MapDelegate(private val contextProvider: () -> Context) {
    protected val context: Context
        get() = contextProvider.invoke()

    var onMapReadyListener: (() -> Unit)? = null
    var onMapMoveListener: (() -> Unit)? = null
    var onMapDestroyedListener: (() -> Unit)? = null

    var currentLocation: LatLng = LatLng.ZERO
        private set

    var isMapReady: Boolean = false
        private set

    protected var map: MapboxMap? = null
    protected var isTracking: Boolean = false
    protected var mapDrawManager: MapDrawManager? = null


    private var style: Style? = null
    private var mapView: MapView? = null
    private var symbolManager: SymbolManager? = null
    private var locationComponent: LocationComponent? = null
    private var isZoomEnabled: Boolean = true

    private val pendingActions: MutableList<(MapboxMap) -> Unit> = mutableListOf()

    @SuppressLint("MissingPermission")
    open fun onMapReady(mapView: MapView, map: MapboxMap) {
        this.mapView = mapView
        this.map = map
        map.addOnMoveListener(onMoveListener)
        if (map.style == null) {
            map.setStyle(Style.Builder().fromUri(MAP_BOX_STYLE_CUSTOM)) { style ->
                this.style = style

                with(map) {
                    with(locationComponent) {
                        val locationComponentOptions = LocationComponentOptions.builder(context)
                            .elevation(5.0f)
                            .accuracyAlpha(0.6f)
                            .build()
                        val options = LocationComponentActivationOptions
                            .builder(context, style)
                            .locationComponentOptions(locationComponentOptions)
                            .build()
                        activateLocationComponent(options)
                        isLocationComponentEnabled = true
                        cameraMode = CameraMode.NONE;
                        renderMode = RenderMode.GPS
                    }
                    with(uiSettings) {
                        isCompassEnabled = true
                        isAttributionEnabled = false
                        isLogoEnabled = false
                    }

                    if (isZoomEnabled) {
                        setMinZoomPreference(MIN_ZOOM)
                        setMaxZoomPreference(MAX_ZOOM)
                    }
                }
                this@MapDelegate.symbolManager = SymbolManager(mapView, map, style)
                this@MapDelegate.mapDrawManager = MapDrawManager(context, style)
            }
            this@MapDelegate.locationComponent = map.locationComponent

            pendingActions.forEach { it.invoke(map) }
            pendingActions.clear()
        }
        onMapReadyListener?.invoke()
        isMapReady = true
    }

    open fun onCreate(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
    }

    open fun onSaveInstanceState(outState: Bundle) {
        mapView?.onSaveInstanceState(outState)
    }

    open fun onStart() {
        mapView?.onStart()
    }

    open fun onStop() {
        mapView?.onStop()
    }

    open fun onResume() {
        mapView?.onResume()
    }

    open fun onPause() {
        mapView?.onPause()
    }

    open fun onLowMemory() {
        mapView?.onLowMemory()
    }

    open fun onDestroy() {
        onMapDestroyedListener?.invoke()
        mapDrawManager?.flush()

        mapView?.onDestroy()
        mapView = null

        map?.removeOnMoveListener(onMoveListener)
        map = null

        onMapReadyListener = null
        onMapMoveListener = null
        locationComponent = null
        mapDrawManager = null
        symbolManager = null
        isMapReady = false
    }

    open fun setIsTracking(isTracking: Boolean) {
        this.isTracking = isTracking

        if (isMapReady && isTracking)
            map?.animateCamera(
                CameraUpdateFactory.newLatLng(
                    com.mapbox.mapboxsdk.geometry.LatLng(currentLocation.lat, currentLocation.lng)
                )
            )
    }

    open fun setOriginLocation(location: LatLng) {
        currentLocation = location

        val action = { map: MapboxMap ->
            locationComponent?.forceLocationUpdate(Location(LocationManager.GPS_PROVIDER).apply {
                longitude = currentLocation.lng
                latitude = currentLocation.lat
            })

            if (isTracking)
                map.animateCamera(
                    CameraUpdateFactory.newLatLng(
                        com.mapbox.mapboxsdk.geometry.LatLng(currentLocation.lat, currentLocation.lng)
                    )
                )
        }

        if (isMapReady) {
            map?.let(action)
        } else pendingActions.add(action)
    }

    fun setFocusInBounds(start: LatLng, end: LatLng) {
        val latNorth = if (start.lat > end.lat) start.lat else end.lat
        val latSouth = if (start.lat < end.lat) start.lat else end.lat
        val lonEast = if (start.lng > end.lng) start.lng else end.lng
        val lonWest = if (start.lng < end.lng) start.lng else end.lng

        val action = { map: MapboxMap ->
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds.from(
                        latNorth, lonEast, latSouth, lonWest
                    ), 64
                )
            )
        }

        if (isMapReady) {
            map?.let(action)
        } else pendingActions.add(action)
    }

    fun withNavigation(): NavigationMapDelegate {
        return NavigationMapDelegate { context }
    }

    fun disableZoomBounds() {
        this.isZoomEnabled = false
    }

    private val onMoveListener = object : MapboxMap.OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {

        }

        override fun onMove(detector: MoveGestureDetector) {
            onMapMoveListener?.invoke()
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }
    }

    companion object {
        const val MAP_BOX_STYLE_CUSTOM = "mapbox://styles/harperjr/ck9smyr8w003m1inv1tsctb7e"
    }
}