package com.harper.carnet.ui.settings.regions

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.regions.RegionsProvider
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ext.cast
import com.harper.carnet.ext.rxLiveData
import com.harper.carnet.ui.settings.regions.service.RegionLoadingService

class RegionsViewModel(private val regionsProvider: RegionsProvider) : ViewModel() {
    val regionsLiveDate: LiveData<List<Region>> = rxLiveData { regionsProvider.provideRegions() }
    val binderLiveData: MutableLiveData<RegionLoadingService.RegionLoadingBinder> = MutableLiveData()

    fun getServiceConnection() = serviceConnection

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            binderLiveData.postValue(null)
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            binderLiveData.postValue(service.cast<RegionLoadingService.RegionLoadingBinder>())
        }
    }
}