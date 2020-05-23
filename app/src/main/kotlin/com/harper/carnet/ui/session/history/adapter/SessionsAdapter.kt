package com.harper.carnet.ui.session.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.ext.cast
import com.harper.carnet.ui.map.delegate.MapDelegate
import com.harper.carnet.ui.support.ValueFormatter
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.include_session_values.*
import kotlinx.android.synthetic.main.item_session_history.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SessionsAdapter(private val contextProvider: () -> Context) :
    ListAdapter<Session, SessionsAdapter.ViewHolder>(DiffUtilCallback) {
    private val context: Context
        get() = contextProvider.invoke()
    private val timeFormatter: DateFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_session_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Session) {
            bindMap(item)
            bindValues(item.values)

            origin.text = context.getString(
                R.string.session_history_location,
                timeFormatter.format(item.startTime),
                item.startLocation.name
            )

            destination.text = context.getString(
                R.string.session_history_location,
                timeFormatter.format(item.endTime),
                item.endLocation.name
            )

            valueWarnings.text = item.notifications.count().toString()
            valueRotates.text = "0"
        }

        private fun bindMap(item: Session) {
            val start = item.startLocation.latLng
            val end = item.endLocation.latLng

            val latNorth = if (start.lat > end.lat) start.lat else end.lat
            val latSouth = if (start.lat < end.lat) start.lat else end.lat
            val lonEast = if (start.lng > end.lng) start.lng else end.lng
            val lonWest = if (start.lng < end.lng) start.lng else end.lng

            mapView.doOnPreDraw { imageView ->
                loadMapRegionIntoView(imageView.cast(), latNorth, latSouth, lonEast, lonWest)
            }
        }

        private fun loadMapRegionIntoView(
            imageView: ImageView,
            latNorth: Double,
            latSouth: Double,
            lonEast: Double,
            lonWest: Double
        ) {
            val options = MapSnapshotter.Options(imageView.width, imageView.height)
                .withRegion(LatLngBounds.from(latNorth, lonEast, latSouth, lonWest))
                .withStyleBuilder(Style.Builder().fromUri(MapDelegate.MAP_BOX_STYLE_CUSTOM))
                .withLogo(false)

            MapSnapshotter(context, options)
                .start { imageView.setImageBitmap(it.bitmap) }
        }

        private fun bindValues(values: List<Value<*>>) {
            for (value in values) {
                val viewId = resolveValueId(value)
                if (viewId != -1)
                    itemView.findViewById<TextView>(viewId).text = ValueFormatter.format(value)
            }
        }

        private fun resolveValueId(value: Value<*>): Int {
            return VALUE_IDS[value.type] ?: -1
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return false
        }
    }

    companion object {
        private val VALUE_IDS = mapOf(
            ValueType.VOLTAGE to R.id.valueAccumulator,
            ValueType.SPEED to R.id.valueSpeed,
            ValueType.FUEL_LEVEL to R.id.valueFuel
        )
    }
}
