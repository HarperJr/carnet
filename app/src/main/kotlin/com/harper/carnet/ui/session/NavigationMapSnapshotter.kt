package com.harper.carnet.ui.session

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.harper.carnet.R
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Created by HarperJr on 12:11
 **/
object NavigationMapSnapshotter {

    fun snapIntoImageView(imageView: ImageView, bounds: LatLngBounds, route: List<LatLng>) {
        val styleBuilder = Style.Builder().fromUri(MapDelegate.MAP_BOX_STYLE_CUSTOM)

        if (route.isNotEmpty()) {
            val sourceId = "path-source-id"
            val feature = Feature.fromGeometry(LineString.fromLngLats(route.map { Point.fromLngLat(it.lat, it.lng) }))
            styleBuilder.withSource(
                GeoJsonSource(
                    sourceId,
                    FeatureCollection.fromFeature(feature)
                )
            ).withLayer(
                LineLayer("path-layer", sourceId).withProperties(
                    PropertyFactory.lineColor(ContextCompat.getColor(imageView.context, R.color.colorAccent)),
                    PropertyFactory.lineOpacity(0.8f),
                    PropertyFactory.lineWidth(4.5f)
                )
            )
        }

        val options = MapSnapshotter.Options(imageView.width, imageView.height)
            .withRegion(LatLngBounds.from(bounds.latNorth, bounds.lonEast, bounds.latSouth, bounds.lonWest))
            .withStyleBuilder(styleBuilder)
            .withLogo(false)

        MapSnapshotter(imageView.context, options)
            .start { imageView.setImageBitmap(it.bitmap) }
    }
}