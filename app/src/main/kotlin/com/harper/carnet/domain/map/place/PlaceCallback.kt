package com.harper.carnet.domain.map.place

import com.mapbox.api.geocoding.v5.models.CarmenFeature

interface PlaceCallback {

    fun onSuccess(features: List<CarmenFeature>)

    fun onFail(throwable: Throwable)
}
