package com.harper.carnet.ui.settings.regions

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.regions.RegionsProvider
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ext.cast
import com.harper.carnet.ext.rxLiveData
import com.harper.carnet.ui.settings.regions.service.RegionLoadProgress
import com.harper.carnet.ui.settings.regions.service.RegionLoadingService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RegionsViewModel(private val regionsProvider: RegionsProvider) : ViewModel() {
    val regionsLiveDate: LiveData<List<Region>> = rxLiveData { regionsProvider.provideRegions() }
    val progressLiveData: LiveData<RegionLoadProgress> = rxLiveData {
        serviceSubject.onErrorResumeNext(Observable.empty())
            .flatMap { it.loadProgressSubject }
    }

    private val serviceSubject: PublishSubject<RegionLoadingService> = PublishSubject.create<RegionLoadingService>()

    fun getServiceConnection() = serviceConnection

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            serviceSubject.onError(IllegalStateException("Service is disconnected"))
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            serviceSubject.onNext(service.cast<RegionLoadingService.RegionLoadingBinder>().getService())
        }
    }
}