package com.harper.carnet.ui.intro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_intro_page.*

class IntroAdapter(context: Context, private val items: List<IntroViewItem>) :
    RecyclerView.Adapter<IntroAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_intro_page, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: IntroViewItem) {
            intro_title.setText(item.titleRes)
            intro_text.setText(item.textRes)
        }
    }
}
