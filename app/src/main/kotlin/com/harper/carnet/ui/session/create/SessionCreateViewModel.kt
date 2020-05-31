package com.harper.carnet.ui.session.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.map.place.PlaceCallback
import com.harper.carnet.domain.map.place.PlaceProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Place
import com.harper.carnet.domain.session.SessionManager
import com.harper.carnet.ext.rxLiveData
import com.mapbox.geojson.BoundingBox
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.cos

class SessionCreateViewModel(
    private val locationProvider: LocationProvider,
    private val sessionManager: SessionManager
) : ViewModel() {
    val searchHintsLiveData: LiveData<List<Place>> = rxLiveData {
        searchHintsSubject.debounce(SEARCH_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .flatMap { createPlaceRequestObservable(it) }
    }
    val selectedPlaceLiveData: MutableLiveData<Place> = MutableLiveData()
    val currentLocationLiveData: MutableLiveData<LatLng> = MutableLiveData()
    val sessionCreatedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val searchHintsSubject = PublishSubject.create<String>()

    private var placeDisposable = Disposables.disposed()
    private var locationDisposable = Disposables.disposed()
    private var createSessionDisposable = Disposables.disposed()
    private var preferredSearchDistance: Float = DEFAULT_SEARCH_DISTANCE_KM
    private var currentPlace: Place? = null

    override fun onCleared() {
        createSessionDisposable.dispose()
        locationDisposable.dispose()
        placeDisposable.dispose()
    }

    fun onSearchUpdated(query: String) {
        searchHintsSubject.onNext(query)
    }

    fun onSearchItemClicked(item: Place) {
        selectedPlaceLiveData.value = item
    }

    fun startLocationUpdates() {
        locationProvider.startRequesting()
        locationDisposable = locationProvider.updates()
            .subscribe {
                requestCurrentLocationPlace(it)
                currentLocationLiveData.value = it
            }
    }

    fun stopLocationUpdates() {
        locationProvider.stopRequesting()
        locationDisposable.dispose()
    }

    private fun createPlaceRequestObservable(query: String): Observable<List<Place>> {
        return Observable.create { sub ->
            PlaceProvider.provide(
                boundary = adjustBoundary(currentLocationLiveData.value),
                query = query,
                callback = object : PlaceCallback {

                    override fun onSuccess(places: List<Place>) {
                        if (places.isEmpty()) {
                            preferredSearchDistance *= 1.5f
                        } else preferredSearchDistance = DEFAULT_SEARCH_DISTANCE_KM

                        sub.onNext(places)
                    }

                    override fun onFail(throwable: Throwable) {
                        sub.onNext(emptyList())
                    }
                })
        }
    }

    private fun requestCurrentLocationPlace(currentLocation: LatLng?) {
        placeDisposable.dispose()
        placeDisposable = Observable.create<Place> { sub ->
            PlaceProvider.provide(location = currentLocation, callback = object : PlaceCallback {

                override fun onSuccess(places: List<Place>) {
                    if (places.isEmpty()) {
                        sub.onError(IllegalStateException("No current place could be found"))
                    } else sub.onNext(places.first())
                }

                override fun onFail(throwable: Throwable) {
                    sub.onError(throwable)
                }
            })
        }.subscribe({
            currentPlace = it
        }, { Timber.e(it) })
    }

    fun onCreateBtnClicked() {
        // TODO БЛЯТЬ ЭТО ЖЕ ПИЗДЕЦ!!!
        if (currentPlace == null) return
        val place = selectedPlaceLiveData.value ?: return

        createSessionDisposable = sessionManager.createSession(currentPlace!!, place)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isCreated ->
                sessionCreatedLiveData.value = isCreated
            }, {
                Timber.e(it)
            })
    }

    private fun adjustBoundary(location: LatLng?): BoundingBox? {
        return location?.let {
            val latDeg = preferredSearchDistance / KM_IN_LAT_DEG
            val lngDeg = (preferredSearchDistance / KM_IN_LNG_DEG) * cos(it.lat)
            BoundingBox.fromLngLats(it.lng - lngDeg, it.lat - latDeg, it.lng + lngDeg, it.lat + latDeg)
        }
    }

    companion object {
        private const val KM_IN_LAT_DEG = 110.574f
        private const val KM_IN_LNG_DEG = 111.320f
        private const val DEFAULT_SEARCH_DISTANCE_KM = 15f
        private const val SEARCH_DEBOUNCE_TIMEOUT = 1000L
    }
}