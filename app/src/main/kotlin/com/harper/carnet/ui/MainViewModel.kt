package com.harper.carnet.ui

import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.telematics.TelematicsProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(
    private val telematicsProvider: TelematicsProvider,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private var telematicsDisposable: Disposable = Disposables.disposed()

    override fun onCleared() {
        telematicsDisposable.dispose()
    }

    fun onStart() {
        locationProvider.startRequesting()
        telematicsDisposable.dispose()
        telematicsDisposable = locationProvider.updates()
            .flatMapCompletable {
                telematicsProvider.sendTelematics(it)
                    .subscribeOn(Schedulers.io())
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Timber.d("Telematics is sent") }, { Timber.e(it) })
    }

    fun onStop() {
        locationProvider.stopRequesting()
        telematicsDisposable.dispose()
    }
}