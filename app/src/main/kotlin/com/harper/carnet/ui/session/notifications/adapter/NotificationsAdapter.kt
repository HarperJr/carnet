package com.harper.carnet.ui.session.notifications.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Notification
import kotlinx.android.extensions.LayoutContainer

class NotificationsAdapter(private val contextProvider: () -> Context) :
    ListAdapter<Notification, NotificationsAdapter.ViewHolder>(DiffUtilCallback) {
    private val context: Context
        get() = contextProvider.invoke()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_session_notification, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Notification) {

        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return false
        }
    }
}
