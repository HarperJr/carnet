package com.harper.carnet.ui.map.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.harper.carnet.domain.model.LatLng
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
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

    protected var map: MapboxMap? = null
    protected var mapDrawManager: MapDrawManager? = null
    protected var isTracking: Boolean = false
    protected var lastLocation: LatLng = LatLng.ZERO


    private var mapView: MapView? = null
    private var style: Style? = null

    private var locationComponent: LocationComponent? = null

    private var symbolManager: SymbolManager? = null

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
                        renderMode = RenderMode.GPS
                    }
                    with(uiSettings) {
                        isCompassEnabled = true
                        isAttributionEnabled = false
                        isLogoEnabled = false
                    }
                    setMinZoomPreference(MIN_ZOOM)
                    setMaxZoomPreference(MAX_ZOOM)
                }
                this@MapDelegate.symbolManager = SymbolManager(mapView, map, style)
                this@MapDelegate.mapDrawManager = MapDrawManager(mapView, map, style)
            }
            this@MapDelegate.locationComponent = map.locationComponent
        }
        onMapReadyListener?.invoke()
    }

    fun setIsTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }

    fun zoomIn() {
        map?.animateCamera(CameraUpdateFactory.zoomIn())
    }

    fun zoomOut() {
        map?.animateCamera(CameraUpdateFactory.zoomOut())
    }

    fun zoom(zoom: Double) {
        map?.animateCamera(CameraUpdateFactory.zoomBy(zoom))
    }

    fun setOriginLocation(location: LatLng) {
        map?.moveCamera(CameraUpdateFactory.newLatLng(com.mapbox.mapboxsdk.geometry.LatLng(location.lat, location.lng)))
        lastLocation = location
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
    }

    fun withNavigation(): NavigationMapDelegate {
        return NavigationMapDelegate { context }
    }

    fun focusInBounds(start: LatLng, end: LatLng) {
        val latNorth = if (start.lat > end.lat) start.lat else end.lat
        val latSouth = if (start.lat < end.lat) start.lat else end.lat
        val lonEast = if(start.lng > end.lng) start.lng else end.lng
        val lonWest = if(start.lng < end.lng) start.lng else end.lng

        map?.setLatLngBoundsForCameraTarget(
            LatLngBounds.from(
                latNorth, lonEast, latSouth, lonWest
            )
        )
    }

    private val onMoveListener = object : MapboxMap.OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {

        }

        override fun onMove(detector: MoveGestureDetector) {
            onMapMoveListener?.invoke()
            isTracking = false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }
    }

    companion object {
        const val MAP_BOX_STYLE_CUSTOM = "mapbox://styles/harperjr/ck9smyr8w003m1inv1tsctb7e"

        private const val MIN_ZOOM = 10.0
        private const val MAX_ZOOM = 20.0
    }
}