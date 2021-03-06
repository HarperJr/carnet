package com.harper.carnet.ui.settings.regions.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.harper.carnet.R
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ext.cast
import com.harper.carnet.ui.support.regions.RegionsLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


/**
 * Created by HarperJr on 15:15
 **/
class RegionLoadingService : Service() {
    val loadProgressSubject = PublishSubject.create<RegionLoadProgress>()

    private val binder: Binder = RegionLoadingBinder()
    private var regionsLoaderDisposable = Disposables.disposed()
    private lateinit var regionsLoader: RegionsLoader

    override fun onCreate() {
        super.onCreate()
        regionsLoader = RegionsLoader(applicationContext)
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showNotification()
        startRegionLoading(intent.getSerializableExtra(REGION_EXTRA).cast())
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        regionsLoaderDisposable.dispose()
        super.onDestroy()
    }

    private fun showNotification() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_arrow_down_collapse)
            .setContentTitle(applicationContext.getString(R.string.region_manager_title))
            .setContentText(applicationContext.getString(R.string.region_manager_text))
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

    private fun startRegionLoading(region: Region) {
        regionsLoader.let { regLoader ->
            regionsLoaderDisposable = Observable.create<Unit> { sub ->
                regLoader.isRegionLoaded(region) { isLoaded ->
                    if (!isLoaded) {
                        kotlin.runCatching {
                            sub.onNext(regLoader.loadRegion(region))
                        }.onFailure { sub.onError(it) }
                    } else sub.onError(IllegalStateException("Region is loaded"))
                }
            }.onErrorResumeNext(Observable.empty())
                .flatMap { regLoader.downloadProgress() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { stopSelf() }
                .subscribe({
                    Timber.d("Region $region download progress: $it")
                    this.loadProgressSubject.onNext(RegionLoadProgress(region, it))
                }, {
                    Timber.e("Region $region download error $it")
                    this.loadProgressSubject.onError(it)
                }, {
                    Timber.e("Region $region is downloaded")
                    this.loadProgressSubject.onComplete()
                })
        }
    }

    inner class RegionLoadingBinder : Binder() {

        fun getService(): RegionLoadingService = this@RegionLoadingService
    }

    companion object {
        const val REGION_EXTRA = "REGION_EXTRA"
        private const val CHANNEL_ID = "REGION_LOADING_SERVICE_NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_ID = 0x1240
    }
}