package com.harper.carnet.domain.map.location

import android.location.Location

interface LocationListener {
    fun onLocationReceived(location: Location)
}
