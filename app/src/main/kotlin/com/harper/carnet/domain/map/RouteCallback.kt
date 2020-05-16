package com.harper.carnet.domain.map

import com.mapbox.api.directions.v5.models.DirectionsRoute

interface RouteCallback {

    fun onSuccess(route: DirectionsRoute)

    fun onFail(throwable: Throwable)
}
