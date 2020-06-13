package com.harper.carnet.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.NotificationSender
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.NotificationType
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.domain.telematics.TelematicsProvider
import com.harper.carnet.ext.rxLiveData

class MapViewModel(
    private val locationProvider: LocationProvider,
    private val sessionProvider: SessionProvider,
    private val telematicsProvider: TelematicsProvider
) : ViewModel() {
    val locationsLiveData: LiveData<LatLng> = rxLiveData { locationProvider.updates() }
    val activeSessionLiveData: LiveData<Session> = rxLiveData { sessionProvider.provideActiveSession() }
    val telematicsLiveData: LiveData<List<Telematics>> = rxLiveData { telematicsProvider.provideTelematics() }
    val originBtnActiveStateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var notificationType: NotificationType? = null

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