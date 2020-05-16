package com.harper.carnet.ui.session.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Session
import kotlinx.android.extensions.LayoutContainer

class SessionsAdapter(private val contextProvider: () -> Context) :
    ListAdapter<Session, SessionsAdapter.ViewHolder>(DiffUtilCallback) {
    private val context: Context
        get() = contextProvider.invoke()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_session_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Session) {

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
}
