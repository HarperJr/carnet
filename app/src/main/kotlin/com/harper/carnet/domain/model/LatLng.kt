package com.harper.carnet.domain.model

data class LatLng(val lat: Double, val lng: Double) {
    companion object {
        val ZERO = LatLng(0.0, 0.0)
    }
}
