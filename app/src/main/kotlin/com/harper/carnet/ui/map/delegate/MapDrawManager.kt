package com.harper.carnet.ui.map.delegate

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

class MapDrawManager(private val mapView: MapView, private val map: MapboxMap, private val style: Style) {
    private var sources: MutableList<String> = mutableListOf()
    private var layers: MutableList<String> = mutableListOf()

    fun drawLine(layerName: String, data: List<Point>): Layer {
        return createLayer<LineLayer>(layerName, data).withProperties(
            PropertyFactory.lineColor(ContextCompat.getColor(mapView.context, R.color.colorAccent)),
            PropertyFactory.lineOpacity(0.7f),
            PropertyFactory.lineWidth(7f)
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
        val layer = when (T::class) {
            LineLayer::class -> {
                val featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(data)))
                val source = style.getSource(sourceId)
                if (source != null) {
                    source.cast<GeoJsonSource>().setGeoJson(featureCollection)
                } else createSource(sourceId, featureCollection)

                LineLayer(layerName, sourceId) as T
            }
            else -> throw IllegalArgumentException("Unable to create layer=$layerName")
        }

        return style.getLayer(layerName)?.cast() ?: layer
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