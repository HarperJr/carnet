package com.harper.carnet.domain.diagnostics

import com.harper.carnet.data.diagnostics.client.DiagnosticsAdapterJson
import com.harper.carnet.data.diagnostics.client.DiagnosticsClient
import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.ValueType
import io.reactivex.Observable

class ValuesProvider(private val diagnosticsClient: DiagnosticsClient) {
    private val diagnosticsAdapterJson: DiagnosticsAdapterJson = DiagnosticsAdapterJson(diagnosticsClient)

    fun provideValues(): Observable<List<DiagnosticValue<*>>> {
        return diagnosticsAdapterJson.getDiagnosticValues()
    }
}