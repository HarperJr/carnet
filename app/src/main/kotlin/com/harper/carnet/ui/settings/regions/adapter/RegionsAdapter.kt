package com.harper.carnet.ui.settings.regions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ui.support.FileSizeFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_region.*

class RegionsAdapter(private val contextProvider: () -> Context, private val onItemClickedCallback: (Region) -> Unit) :
    ListAdapter<Region, RegionsAdapter.ViewHolder>(DiffUtilCallback) {
    private val context: Context
        get() = contextProvider.invoke()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_region, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Region) {
            itemView.setOnClickListener { onItemClickedCallback.invoke(item) }
            regionSize.text = FileSizeFormatter.format(item.size)
            regionText.text = item.name
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<Region>() {

        override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
            return false
        }
    }
}