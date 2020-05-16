package com.harper.carnet.domain.map

import android.location.Location

interface LocationsListener {
    fun onLocationReceived(location: Location)
}
