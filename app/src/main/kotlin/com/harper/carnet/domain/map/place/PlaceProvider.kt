package com.harper.carnet.domain.map.place

import com.harper.carnet.BuildConfig
import com.harper.carnet.domain.model.LatLng
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

    fun provide(location: LatLng?, proximity: LatLng?, place: String?, callback: PlaceCallback) {
        val requestBuilder = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_TOKEN)

        when {
            place != null -> requestBuilder.query(place)
            location != null -> requestBuilder.query(Point.fromLngLat(location.lng, location.lat))
            else -> throw IllegalArgumentException("Unable to provide place with no latlng or place provided")
        }

        if (proximity != null)
            requestBuilder.proximity(Point.fromLngLat(proximity.lng, proximity.lat))

        val request = requestBuilder.build()
        request.enqueueCall(object : Callback<GeocodingResponse> {

            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                val body = response.body()
                if (body == null) {
                    callback.onFail(IllegalStateException("Route body is null"))
                } else callback.onSuccess(body.features())
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                callback.onFail(t)
            }
        })
    }
}