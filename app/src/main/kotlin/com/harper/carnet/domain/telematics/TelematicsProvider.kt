package com.harper.carnet.domain.telematics

import com.harper.carnet.data.api.ApiExecutor
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Telematics
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by HarperJr on 1:36
 **/
class TelematicsProvider(private val apiExecutor: ApiExecutor, private val appStorage: AppStorage) {

    fun provideTelematics(): Observable<List<Telematics>> {
        return apiExecutor.getTelematics()
            .toObservable()
    }

    fun authDevice(deviceIdentity: String): Completable {
        return apiExecutor.authDevice(deviceIdentity)
            .doOnSuccess { appStorage.setJwtToken(it) }
            .ignoreElement()
    }

    fun regDevice(deviceIdentity: String, pushToken: String): Completable {
        return apiExecutor.regDevice(deviceIdentity, pushToken)
            .doOnSuccess { appStorage.setJwtToken(it) }
            .ignoreElement()
    }

    fun sendTelematics(latLng: LatLng): Completable {
        return apiExecutor.sendTelematics(latLng.lat, latLng.lng, 0.0, 0.0)
    }

    fun subscribeNotifications(pushToken: String): Completable {
        return apiExecutor.subscribeNotifications(pushToken)
    }
}