package com.harper.carnet.domain.map.location

import android.content.Context
import com.harper.carnet.domain.model.LatLng
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit


class LocationProvider(private val context: Context) {
    private val locationEngine = LocationEngineProvider.getBestLocationEngine(context)
    private val updatesSubj: PublishSubject<LatLng> = PublishSubject.create()
    private var requestingUpdates = false

    @Throws(SecurityException::class)
    fun startRequesting() {
        if (requestingUpdates) return
        requestingUpdates = true

        val request = LocationEngineRequest.Builder(TimeUnit.SECONDS.toMillis(2))
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(TimeUnit.SECONDS.toMillis(10)).build()

        locationEngine.requestLocationUpdates(request, locationCallback, context.mainLooper)
        locationEngine.getLastLocation(locationCallback)
    }

    fun stopRequesting() {
        if (!requestingUpdates) return
        requestingUpdates = false

        locationEngine
            .removeLocationUpdates(locationCallback)
    }

    fun updates(): Observable<LatLng> = updatesSubj

    private val locationCallback = object : LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?) {
            if (result != null) {
                result.lastLocation?.also {
                    updatesSubj.onNext(LatLng(it.latitude, it.longitude))
                }
            }
        }

        override fun onFailure(exception: Exception) {
            Timber.e(exception)
        }
    }
}