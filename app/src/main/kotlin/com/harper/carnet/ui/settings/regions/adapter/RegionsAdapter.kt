package com.harper.carnet.ui.settings.regions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ui.support.FileSizeFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_region.*
import kotlinx.android.synthetic.main.item_region.view.*
import timber.log.Timber

class RegionsAdapter(private val contextProvider: () -> Context, private val onItemClickedCallback: (Region) -> Unit) :
    RecyclerView.Adapter<RegionsAdapter.ViewHolder>() {
    private val context: Context
        get() = contextProvider.invoke()
    private var items: List<Region> = emptyList()
    private var regionsLoadProgress: HashMap<Int, Int> = hashMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_region, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads.first() == LOAD_PROGRESS_CHANGE_PAYLOAD)
                holder.setLoadProgress(regionsLoadProgress[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun submitList(items: List<Region>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setRegionLoadProgress(region: Region, value: Int) {
        val itemPosition = items.indexOfFirst { it.code == region.code }
            .also { regionsLoadProgress[it] = value }
        notifyItemChanged(itemPosition, LOAD_PROGRESS_CHANGE_PAYLOAD)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Region) {
            itemView.setOnClickListener { onItemClickedCallback.invoke(item) }
            regionSize.text = FileSizeFormatter.format(item.size)
            regionText.text = item.name
        }

        fun setLoadProgress(progress: Int) {
            itemView.loadProgress.progress = progress
        }
    }

    companion object {
        private const val LOAD_PROGRESS_CHANGE_PAYLOAD = "LOAD_PROGRESS_CHANGE_PAYLOAD"
    }
}