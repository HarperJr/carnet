package com.harper.carnet.data.diagnostics.client

import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.ValueType
import io.reactivex.Observable

/**
 * Created by HarperJr on 23:12
 **/
class DiagnosticsAdapterJson(private val diagnosticsClient: DiagnosticsClient) {
    fun getDiagnosticValues(): Observable<List<DiagnosticValue<*>>> {
        return diagnosticsClient.getDiagnostics()
            .map { rawLog ->
                if (rawLog.isEmpty()) {
                    emptyList<DiagnosticValue<*>>()
                } else rawLog.split(" ").mapIndexed { index, value ->
                        DiagnosticValue(VALUE_TYPE_ORDER[index], value.toFloat())
                    }
            }
    }

    companion object {
        private val VALUE_TYPE_ORDER = listOf(
            ValueType.VOLTAGE,
            ValueType.ENGINE_TEMPERATURE,
            ValueType.TIME_RUN,
            ValueType.SPEED,
            ValueType.FUEL_LEVEL
        )
    }
}