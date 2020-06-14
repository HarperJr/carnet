package com.harper.carnet.ui.support

import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.ValueType
import java.text.DecimalFormat

object ValueFormatter {
    private val FORMAT_PERCENT = DecimalFormat("#%")
    private val FORMAT_CELSIUS = DecimalFormat("#°C")
    private val FORMAT_VOLTAGE = DecimalFormat("#.#V")
    private val FORMAT_PRESSURE = DecimalFormat("#Ph")
    private val FORMAT_HOURS = DecimalFormat("#H")

    private val VALUE_FORMAT: Map<ValueType, DecimalFormat> = mapOf(
        ValueType.FUEL_PRESSURE to FORMAT_PRESSURE,
        ValueType.VOLTAGE to FORMAT_VOLTAGE,
        ValueType.FUEL_LEVEL to FORMAT_PERCENT,
        ValueType.ENGINE_TEMPERATURE to FORMAT_CELSIUS,
        ValueType.ENGINE_LOAD to FORMAT_PERCENT,
        ValueType.TIME_RUN to FORMAT_HOURS
    )

    fun format(diagnosticValue: DiagnosticValue<*>): String {
        val unformattedValue = diagnosticValue.value
        return VALUE_FORMAT[diagnosticValue.type]?.format(unformattedValue) ?: unformattedValue.toString()
    }
}