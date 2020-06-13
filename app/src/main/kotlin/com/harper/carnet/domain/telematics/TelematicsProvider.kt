package com.harper.carnet.domain.telematics

import com.harper.carnet.data.api.ApiExecutor
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.domain.model.Telematics
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by HarperJr on 1:36
 **/
class TelematicsProvider(private val apiExecutor: ApiExecutor, private val appStorage: AppStorage) {

    fun provideTelematics(): Observable<List<Telematics>> = Single.just(appStorage.getDeviceIdentity())
        .flatMapObservable { identity ->
            if (identity.isEmpty()) {
                Observable.error(Throwable("Device identity is empty"))
            } else requestTelematics()
        }.subscribeOn(Schedulers.io())

    private fun requestTelematics() = Observable.timer(TELEMATICS_DELAY, TimeUnit.MILLISECONDS)
        .flatMapSingle {
            apiExecutor.getTelematics()
        }

    companion object {
        private const val TELEMATICS_DELAY = 1000L
    }
}