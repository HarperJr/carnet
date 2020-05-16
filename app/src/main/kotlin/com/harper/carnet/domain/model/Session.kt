package com.harper.carnet.domain.model

import java.util.*

data class Session(val dateStart: Date, val dateEnd: Date, val startLocation: LatLng, val endLocation: LatLng) {
    companion object {
        val EMPTY = Session(Date(), Date(), LatLng(0.0, 0.0), LatLng(0.0, 0.0))
    }
}