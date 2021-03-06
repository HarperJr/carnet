package com.harper.carnet.domain.model

import com.harper.carnet.ext.cast
import java.util.*

class Session(
    val id: Int,
    val startTime: Date,
    val endTime: Date?,
    val isActive: Boolean,
    val startLocation: Location,
    val endLocation: Location,
    val routePath: List<LatLng> = emptyList(),
    val diagnosticValues: List<DiagnosticValue<*>> = emptyList(),
    val notifications: List<Notification> = emptyList()
) {

    companion object {
        val EMPTY = Session(
            -1,
            Date(),
            Date(),
            false,
            Location(LatLng(0.0, 0.0), ""),
            Location(LatLng(0.0, 0.0), "")
        )
    }

    override fun equals(other: Any?): Boolean {
        return other?.cast<Session>()?.id == this.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        result = 31 * result + startLocation.hashCode()
        result = 31 * result + endLocation.hashCode()
        result = 31 * result + diagnosticValues.hashCode()
        result = 31 * result + notifications.hashCode()
        result = 31 * result + routePath.hashCode()
        return result
    }
}