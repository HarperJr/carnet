package com.harper.carnet

import android.app.Application
import com.harper.carnet.di.ApiModule
import com.harper.carnet.di.DataModule
import com.harper.carnet.di.DomainModule
import com.harper.carnet.di.UiModule
import com.mapbox.mapboxsdk.Mapbox
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
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(baseContext)
            modules(UiModule(), DomainModule(), DataModule(), ApiModule())
        }
    }
}