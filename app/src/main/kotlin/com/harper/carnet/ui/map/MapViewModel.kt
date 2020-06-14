package com.harper.carnet.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.domain.diagnostics.DiagnosticsProvider
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.domain.telematics.TelematicsProvider
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MapViewModel(
    private val locationProvider: LocationProvider,
    private val sessionProvider: SessionProvider,
    private val telematicsProvider: TelematicsProvider,
    private val diagnosticsProvider: DiagnosticsProvider,
    private val appStorage: AppStorage
) : ViewModel() {
    val locationsLiveData: LiveData<LatLng> = rxLiveData { locationProvider.updates() }
    val activeSessionLiveData: LiveData<Session> = rxLiveData { sessionProvider.provideActiveSession() }
    val telematicsLiveData: LiveData<List<Telematics>> = rxLiveData { telematicsProvider.provideTelematics() }

    private var telematicsDisposable = Disposables.disposed()

    override fun onCleared() {
        telematicsDisposable.dispose()
    }


    fun onStart() {
        telematicsDisposable = diagnosticsProvider.isConnected()
            .flatMapCompletable { isConnected ->
                if (isConnected) {
                    val deviceIdentity = appStorage.getDeviceIdentity()
                    telematicsProvider.authDevice(deviceIdentity)
                        .onErrorResumeNext {
                            telematicsProvider.regDevice(deviceIdentity)
                        }
                } else Completable.complete()
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Timber.d("Authorized successfully") }, { Timber.e(it) })
    }
}