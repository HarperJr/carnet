package com.harper.carnet.domain.map.route

import android.content.Context
import com.harper.carnet.BuildConfig
import com.harper.carnet.domain.model.LatLng
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RouteProvider {

    fun provide(context: Context, origin: LatLng, dest: LatLng, callback: RouteCallback) {
        NavigationRoute.builder(context)
            .accessToken(BuildConfig.MAPBOX_TOKEN)
            .origin(Point.fromLngLat(origin.lng, origin.lat))
            .destination(Point.fromLngLat(dest.lng, dest.lat))
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .enableRefresh(true)
            .build()
            .getRoute(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    val body = response.body()
                    if (body == null) {
                        callback.onFail(IllegalStateException("Route body is null"))
                    } else callback.onSuccess(body.routes().first())
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    callback.onFail(t)
                }
            })
    }
}