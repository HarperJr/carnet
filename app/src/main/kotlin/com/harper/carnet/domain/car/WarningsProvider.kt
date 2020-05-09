package com.harper.carnet.domain.car

import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.domain.model.WarningType
import kotlinx.coroutines.coroutineScope
import java.util.*

class WarningsProvider {
    suspend fun provideWarnings(): List<Warning> = coroutineScope {
        return@coroutineScope listOf(
            Warning(WarningType.LOW_FUEL, Value(ValueType.FUEL_LEVEL, 0.3), Date().apply { time -= 15600 }),
            Warning(WarningType.LOW_VOLTAGE, Value(ValueType.VOLTAGE, 13.3), Date().apply { time -= 75600 })
        )
    }
}