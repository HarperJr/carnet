package com.harper.carnet.ui.session.create.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.SearchHistory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_place.*

class SearchHistoryAdapter(private val contextProvider: () -> Context): ListAdapter<SearchHistory, SearchHistoryAdapter.ViewHolder>(DiffUtilCallback) {
    private val context: Context
        get() = contextProvider.invoke()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_place, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: SearchHistory) {
            placeTypeIcon.setImageResource(R.drawable.ic_place)
            placeName.text = item.place
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<SearchHistory>() {

        override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return false
        }
    }
}
