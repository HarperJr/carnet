package com.harper.carnet.domain.model

import com.harper.carnet.ext.cast
import java.util.*

class Session(
    val id: Int,
    val timeStart: Date,
    val timeEnd: Date,
    val startLocation: Location,
    val endLocation: Location,
    val values: List<Value<*>>,
    val notifications: List<Notification>
) {

    companion object {
        val EMPTY = Session(
            -1,
            Date(),
            Date(),
            Location(LatLng(0.0, 0.0), ""),
            Location(LatLng(0.0, 0.0), ""),
            emptyList(),
            emptyList()
        )
    }

    override fun equals(other: Any?): Boolean {
        return other?.cast<Session>()?.id == this.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + timeStart.hashCode()
        result = 31 * result + timeEnd.hashCode()
        result = 31 * result + startLocation.hashCode()
        result = 31 * result + endLocation.hashCode()
        result = 31 * result + values.hashCode()
        result = 31 * result + notifications.hashCode()
        return result
    }
}