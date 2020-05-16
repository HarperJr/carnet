package com.harper.carnet.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.LocationProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.ext.rxLiveData

class MapViewModel(private val locationProvider: LocationProvider) : ViewModel() {
    val locationsLiveData: LiveData<LatLng> = rxLiveData { locationProvider.updates() }

    val originBtnActiveStateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onMapReady() {
        observeLocationUpdates()
    }

    fun onMapMoved() {
        disposeLocationUpdates()
    }

    fun onUserOriginBtnClicked() {
        observeLocationUpdates()
    }

    private fun observeLocationUpdates() {
        locationProvider.startRequesting()
        originBtnActiveStateLiveData.value = true
    }

    private fun disposeLocationUpdates() {
        locationProvider.stopRequesting()
        originBtnActiveStateLiveData.value = false
    }
}