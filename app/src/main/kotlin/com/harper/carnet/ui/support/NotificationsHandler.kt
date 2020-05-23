package com.harper.carnet.ui.support

import android.content.Context
import com.harper.carnet.R
import com.harper.carnet.domain.model.NotificationType
import com.harper.carnet.domain.model.Value

object NotificationsHandler {
    private val NOTIFICATIONS_ICONS: Map<NotificationType, Int> = mapOf(
        NotificationType.TRAFFIC_CONGESTION to R.drawable.ic_round_alert,
        NotificationType.DANGEROUS_MANEUVER to R.drawable.ic_rotate
    )

    fun resolveIcon(type: NotificationType): Int {
        return NOTIFICATIONS_ICONS[type] ?: throw IllegalArgumentException("Unable to resolve icon for type=$type")
    }

    fun resolveText(context: Context, type: NotificationType): String {
        return when (type) {
            NotificationType.TRAFFIC_CONGESTION -> context.getString(R.string.notif_traffic_congestion)
                NotificationType.DANGEROUS_MANEUVER -> context.getString(R.string.notif_dangerous_maneuver)
        }
    }
}