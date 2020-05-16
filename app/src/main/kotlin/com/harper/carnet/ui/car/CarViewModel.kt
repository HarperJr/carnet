package com.harper.carnet.ui.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.harper.carnet.domain.car.ValuesProvider
import com.harper.carnet.domain.car.WarningsProvider
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Observable

class CarViewModel(
    private val valuesProvider: ValuesProvider,
    private val warningsProvider: WarningsProvider
) : ViewModel() {
    val valuesLiveData: LiveData<List<Value<*>>> = rxLiveData {
        valuesProvider.provideValues()
    }

    val warningMessageLiveData: LiveData<List<Warning>> = rxLiveData {
        warningsProvider.provideWarnings()
    }
}
