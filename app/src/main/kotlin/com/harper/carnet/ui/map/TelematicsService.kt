package com.harper.carnet.ui.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.harper.carnet.R
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.domain.diagnostics.DiagnosticsProvider
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.model.Telematics
import com.harper.carnet.domain.telematics.TelematicsProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by HarperJr on 14:57
 **/
class TelematicsService : Service() {
    private val binder: Binder = TelematicsBinder()

    private val telematicsProvider: TelematicsProvider by inject()
    private val diagnosticsProvider: DiagnosticsProvider by inject()
    private val locationProvider: LocationProvider by inject()
    private val appStorage: AppStorage by inject()

    private val telematicsSubject: PublishSubject<List<Telematics>> = PublishSubject.create()

    private var telematicsDisposable: Disposable = Disposables.disposed()
    private var isInitialized = false

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        locationProvider.startRequesting()
    }

    override fun onDestroy() {
        telematicsDisposable.dispose()
        locationProvider.stopRequesting()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isInitialized) {
            showNotification()
            startTelematicsProcessor()

            isInitialized = true
        }

        return START_NOT_STICKY
    }

    private fun startTelematicsProcessor() {
        telematicsDisposable = diagnosticsProvider.isConnected()
            .flatMapCompletable { isConnected ->
                if (isConnected) {
                    Completable.concatArray(requestTelematics(), sendTelematics())
                } else Completable.complete()
            }.subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                authDevice()
            }.subscribe({
                startTelematicsProcessor()
            }, {
                Timber.e(it)
            })
    }

    fun getTelematics(): Observable<List<Telematics>> = telematicsSubject

    private fun requestTelematics() = Observable.timer(TELEMATICS_DELAY, TimeUnit.MILLISECONDS)
        .flatMap {
            telematicsProvider.provideTelematics()
                .doOnNext { telematicsSubject.onNext(it) }
        }.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .ignoreElements()

    private fun sendTelematics() = Observable.timer(TELEMATICS_DELAY, TimeUnit.MILLISECONDS)
        .zipWith(locationProvider.updates())
        .flatMapCompletable { (_, latLng) ->
            telematicsProvider.sendTelematics(latLng)
        }.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())

    private fun authDevice() = Completable.create { sub ->
        val deviceIdentity = appStorage.getDeviceIdentity()
        telematicsProvider.authDevice(deviceIdentity)
            .subscribe({
                telematicsProvider.subscribeNotifications(appStorage.getPushToken())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ Timber.d("Subscribed to notifications successfully") }, { Timber.e(it) })
                sub.onComplete()
            }, {
                telematicsProvider.regDevice(deviceIdentity, appStorage.getPushToken())
                    .subscribe({
                        sub.onComplete()
                    }, {
                        sub.onError(it)
                    })
            })
    }

    private fun showNotification() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(applicationContext.getString(R.string.telematics_title))
            .setContentText(applicationContext.getString(R.string.telematics_text))
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    inner class TelematicsBinder : Binder() {

        fun getService(): TelematicsService = this@TelematicsService
    }

    companion object {
        private const val CHANNEL_ID = "TELEMATICS_SERVICE_NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_ID = 0x1120
        private const val TELEMATICS_DELAY = 1000L
    }
}