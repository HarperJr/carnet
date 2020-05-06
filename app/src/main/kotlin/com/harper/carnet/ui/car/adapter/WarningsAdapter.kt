package com.harper.carnet.ui.car.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.ui.car.adapter.item.WarningsViewItem
import kotlinx.android.extensions.LayoutContainer

class WarningsAdapter(private val contextProvider: () -> Context) : RecyclerView.Adapter<WarningsAdapter.ViewHolder>() {
    private var items: List<WarningsViewItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(contextProvider.invoke()).inflate(R.layout.item_warning, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WarningsViewItem) {
            // Not implemented
        }
    }
}
