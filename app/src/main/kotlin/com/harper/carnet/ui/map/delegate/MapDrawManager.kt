package com.harper.carnet.ui.map.delegate

import android.content.Context
import androidx.core.content.ContextCompat
import com.harper.carnet.R
import com.harper.carnet.ext.cast
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.Source

class MapDrawManager(private val context: Context, private val style: Style) {
    private var sources: MutableList<String> = mutableListOf()
    private var layers: MutableList<String> = mutableListOf()

    fun drawLine(layerName: String, data: List<Point>, color: Int, opacity: Float, width: Float): Layer {
        return createLayer<LineLayer>(layerName, data).withProperties(
            PropertyFactory.lineColor(ContextCompat.getColor(context, color)),
            PropertyFactory.lineOpacity(opacity),
            PropertyFactory.lineWidth(width)
        )
    }

    fun flush() {
        for (source in sources)
            style.removeSource(source)
        sources.clear()

        for (layer in layers)
            style.removeLayer(layer)
        layers.clear()
    }

    private inline fun <reified T : Layer> createLayer(layerName: String, data: List<Point>): T {
        val sourceId = getSourceId(layerName)
        val source = style.getSource(sourceId)

        return when (T::class) {
            LineLayer::class -> {
                val featureCollection =
                    FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(data)))
                if (source == null) {
                    createSource(sourceId, featureCollection)
                } else source.cast<GeoJsonSource>().setGeoJson(featureCollection)

                LineLayer(layerName, sourceId) as T
            }
            else -> throw IllegalArgumentException("Unable to create layer=$layerName")
        }.also {
            if (style.getLayer(layerName) == null) {
                style.addLayer(it)
                layers.add(it.id)
            }
        }
    }

    private fun createSource(sourceId: String, featureCollection: FeatureCollection): Source {
        return GeoJsonSource(sourceId, featureCollection)
            .also {
                style.addSource(it)
                sources.add(it.id)
            }
    }

    private fun getSourceId(layerName: String): String {
        return "$layerName-source"
    }
}