package com.harper.carnet.ui.support

import android.content.Context
import com.harper.carnet.R
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.WarningType

object WarningsHandler {
    private val VALUES_ICONS: Map<WarningType, Int> = mapOf(
        WarningType.LOW_FUEL to R.drawable.ic_fuel,
        WarningType.HEAT_ENGINE to R.drawable.ic_engine,
        WarningType.LOW_VOLTAGE to R.drawable.ic_accumulator,
        WarningType.SPEED_LIMIT to R.drawable.ic_speedometer
    )

    fun resolveIcon(type: WarningType): Int {
        return VALUES_ICONS[type] ?: throw IllegalArgumentException("Unable to resolve icon for type=$type")
    }

    fun resolveText(context: Context, type: WarningType, value: Value<*>): String {
        val formattedValue = ValueFormatter.format(value)
        return when (type) {
            WarningType.LOW_FUEL -> context.getString(R.string.warn_low_fuel, formattedValue)
            WarningType.HEAT_ENGINE -> context.getString(R.string.warn_heat_engine, formattedValue)
            WarningType.LOW_VOLTAGE -> context.getString(R.string.warn_low_voltage, formattedValue)
            WarningType.SPEED_LIMIT -> context.getString(R.string.warn_speed_limit, formattedValue)
        }
    }
}