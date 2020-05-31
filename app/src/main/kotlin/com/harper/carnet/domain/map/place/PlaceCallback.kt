package com.harper.carnet.domain.map.place

import com.harper.carnet.domain.model.Place

interface PlaceCallback {

    fun onSuccess(places: List<Place>)

    fun onFail(throwable: Throwable)
}
