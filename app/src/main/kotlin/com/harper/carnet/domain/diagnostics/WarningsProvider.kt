package com.harper.carnet.domain.diagnostics

import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.domain.model.WarningType
import io.reactivex.Observable
import java.util.*

class WarningsProvider {
    fun provideWarnings(): Observable<List<Warning>> {
        return Observable.just(
            listOf(
                Warning(WarningType.LOW_FUEL, DiagnosticValue(ValueType.FUEL_LEVEL, 0.3), Date().apply { time -= 15600 }),
                Warning(WarningType.LOW_VOLTAGE, DiagnosticValue(ValueType.VOLTAGE, 13.3), Date().apply { time -= 75600 })
            )
        )
    }
}