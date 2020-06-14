package com.harper.carnet.ui.map

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.domain.telematics.TelematicsProvider
import com.harper.carnet.ext.cast
import com.harper.carnet.ext.rxLiveData
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MapViewModel(
    private val locationProvider: LocationProvider,
    private val sessionProvider: SessionProvider
) : ViewModel() {
    val locationsLiveData: LiveData<LatLng> = rxLiveData { locationProvider.updates() }
    val activeSessionLiveData: LiveData<Session> = rxLiveData { sessionProvider.provideActiveSession() }
    val telematicsLiveData: LiveData<List<Telematics>> = rxLiveData {
        serviceSubject.flatMap { it.getTelematics() }
    }

    private val serviceSubject: PublishSubject<TelematicsService> = PublishSubject.create<TelematicsService>()
    private var telematicsDisposable = Disposables.disposed()

    override fun onCleared() {
        telematicsDisposable.dispose()
    }

    fun getServiceConnection() = serviceConnection

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            serviceSubject.onError(IllegalStateException("Service is disconnected"))
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            serviceSubject.onNext(service.cast<TelematicsService.TelematicsBinder>().getService())
        }
    }
}