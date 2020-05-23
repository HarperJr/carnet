package com.harper.carnet.ui.settings.regions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.regions.RegionsProvider
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ext.rxLiveData

class RegionsViewModel(private val regionsProvider: RegionsProvider) : ViewModel() {
    val regionsLiveDate: LiveData<List<Region>> = rxLiveData { regionsProvider.provideRegions() }
}