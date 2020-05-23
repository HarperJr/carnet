package com.harper.carnet.domain.map.location

import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.harper.carnet.domain.model.LatLng
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class LocationProvider(context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val updatesSubj: PublishSubject<LatLng> = PublishSubject.create()
    private val locationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(20)
        maxWaitTime = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(10)
    }
    private var requestingUpdates = false

    @Throws(SecurityException::class)
    fun startRequesting() {
        if (requestingUpdates) return
        requestingUpdates = true

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        fusedLocationClient.lastLocation
            .addOnCompleteListener {
                val lastLocation = it.result
                if (lastLocation != null)
                    updatesSubj.onNext(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
    }

    fun stopRequesting() {
        if (!requestingUpdates) return
        requestingUpdates = false

        fusedLocationClient
            .removeLocationUpdates(locationCallback)
    }

    fun updates(): Observable<LatLng> = updatesSubj

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                val lastLocation = locationResult.lastLocation
                updatesSubj.onNext(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }
    }
}