package com.harper.carnet.domain.map.place

import com.harper.carnet.BuildConfig
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Place
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by HarperJr on 15:26
 **/
object PlaceProvider {

    fun provide(
        location: LatLng? = null,
        proximity: LatLng? = null,
        boundary: BoundingBox? = null,
        query: String? = null,
        callback: PlaceCallback
    ) {
        val requestBuilder = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_TOKEN)

        when {
            query != null -> requestBuilder.query(query)
            location != null -> requestBuilder.query(Point.fromLngLat(location.lng, location.lat))
            else -> throw IllegalArgumentException("Unable to provide query with no latlng or match query provided")
        }

        if (boundary != null)
            requestBuilder.bbox(boundary)

        if (proximity != null)
            requestBuilder.proximity(Point.fromLngLat(proximity.lng, proximity.lat))

        val request = requestBuilder.build()
        request.enqueueCall(object : Callback<GeocodingResponse> {

            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                val body = response.body()
                if (body == null) {
                    callback.onFail(IllegalStateException("Route body is null"))
                } else {
                    val places = mutableListOf<Place>()
                    for (feature in body.features()) {
                        val placeName = feature.placeName()
                        val placeLocation = feature.center()?.let { LatLng(it.latitude(), it.longitude()) }
                        if (placeName != null && placeLocation != null) {
                            places.add(Place(placeName, placeLocation, feature.placeType()))
                        }
                    }

                    callback.onSuccess(places)
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                callback.onFail(t)
            }
        })
    }
}