package com.harper.carnet

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.di.ApiModule
import com.harper.carnet.di.DataModule
import com.harper.carnet.di.DomainModule
import com.harper.carnet.di.UiModule
import com.mapbox.mapboxsdk.Mapbox
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Mapbox.getInstance(baseContext, BuildConfig.MAPBOX_TOKEN)

        startKoin()
        startPushes()
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(baseContext)
            modules(UiModule(), DomainModule(), DataModule(), ApiModule())
        }
    }

    private fun startPushes() {
        FirebaseApp.initializeApp(this)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful)
                    return@OnCompleteListener

                task.result?.token?.let { pushToken ->
                    get<AppStorage>().setPushToken(pushToken)
                }
            })
    }
}