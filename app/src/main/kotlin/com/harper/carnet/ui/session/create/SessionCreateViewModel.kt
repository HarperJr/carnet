package com.harper.carnet.ui.session.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.map.place.PlaceCallback
import com.harper.carnet.domain.map.place.PlaceProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Place
import com.harper.carnet.domain.model.SearchHistory
import com.harper.carnet.ext.rxLiveData
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SessionCreateViewModel(private val locationProvider: LocationProvider) : ViewModel() {
    val searchHintsLiveData: LiveData<List<Place>> = rxLiveData {
        searchHintsSubject.debounce(SEARCH_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .flatMap { createPlaceRequestObservable(it) }
    }

    val searchHintLiveData: MutableLiveData<Place> = MutableLiveData()

    private val searchHintsSubject = PublishSubject.create<String>()
    private var locationUpdatesDisposable = Disposables.disposed()
    private var preferredRadius: Float = DEFAULT_PREFERRED_RADIUS
    private var prevLocation: LatLng? = null

    fun onSearchUpdated(query: String) {
        searchHintsSubject.onNext(query)
    }

    fun startLocationUpdates() {
        locationProvider.startRequesting()
        locationUpdatesDisposable = locationProvider.updates()
            .subscribe { prevLocation = it }

    }

    fun stopLocationUpdates() {
        locationProvider.stopRequesting()
        locationUpdatesDisposable.dispose()
    }

    fun onSearchItemClicked(item: Place) {
        searchHintLiveData.value = item
    }

    private fun createPlaceRequestObservable(query: String): Observable<List<Place>> {
        return Observable.create { sub ->
            PlaceProvider.provide(null, prevLocation, query, object : PlaceCallback {

                override fun onSuccess(features: List<CarmenFeature>) {
                    val results = mutableListOf<Place>()
                    for (feature in features)
                        if (feature.placeName() != null) {
                            val location = feature.center()?.let { LatLng(it.latitude(), it.longitude()) }
                            results.add(Place(feature.placeName()!!, feature.placeType(), location))
                        }
                    if (results.isEmpty()) {
                        preferredRadius += DEFAULT_PREFERRED_RADIUS
                    } else preferredRadius = DEFAULT_PREFERRED_RADIUS
                    sub.onNext(results)
                }

                override fun onFail(throwable: Throwable) {
                    sub.onNext(emptyList())
                }
            })
        }
    }

    fun onCreateBtnClicked() {

    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIMEOUT = 1000L
        private const val DEFAULT_PREFERRED_RADIUS = 10000f
    }
}