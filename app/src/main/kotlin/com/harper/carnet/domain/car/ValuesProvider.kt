package com.harper.carnet.domain.car

import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.ValueType
import kotlinx.coroutines.coroutineScope

class ValuesProvider {
    suspend fun provideValues(): List<Value<*>> = coroutineScope {
        return@coroutineScope listOf(
            Value(ValueType.VOLTAGE, 13.3),
            Value(ValueType.ENGINE_TEMPERATURE, 36),
            Value(ValueType.TIME_RUN, 112),
            Value(ValueType.SPEED, 54),
            Value(ValueType.FUEL_LEVEL, 0.3)
        )
    }
}