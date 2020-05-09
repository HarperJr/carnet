package com.harper.carnet.ui.car.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.ui.support.TimeFormatter
import com.harper.carnet.ui.support.WarningsHandler
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_warning.*

class WarningsAdapter(private val contextProvider: () -> Context) : RecyclerView.Adapter<WarningsAdapter.ViewHolder>() {
    private val context: Context
        get() = contextProvider.invoke()
    private var items: List<Warning> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_warning, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<Warning>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Warning) {
            warningIcon.setImageResource(WarningsHandler.resolveIcon(item.type))
            warningText.text = WarningsHandler.resolveText(context, item.type, item.value)
            warningTime.text = TimeFormatter.format(context, item.time)
        }
    }
}
