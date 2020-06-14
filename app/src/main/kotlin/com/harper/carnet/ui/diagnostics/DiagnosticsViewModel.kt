package com.harper.carnet.ui.diagnostics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.diagnostics.DiagnosticsProvider
import com.harper.carnet.domain.diagnostics.ValuesProvider
import com.harper.carnet.domain.diagnostics.WarningsProvider
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.ext.rxLiveData

class DiagnosticsViewModel(
    private val valuesProvider: ValuesProvider,
    private val warningsProvider: WarningsProvider,
    private val diagnosticsProvider: DiagnosticsProvider
) : ViewModel() {
    val valuesLiveData: LiveData<List<Value<*>>> = rxLiveData {
        valuesProvider.provideValues()
    }

    val warningMessageLiveData: LiveData<List<Warning>> = rxLiveData {
        warningsProvider.provideWarnings()
    }

    val connectionLiveData: LiveData<Boolean> = rxLiveData {
        diagnosticsProvider.isConnected()
    }
}