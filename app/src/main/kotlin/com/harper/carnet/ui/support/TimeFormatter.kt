package com.harper.carnet.ui.support

import android.content.Context
import com.harper.carnet.R
import java.util.*

object TimeFormatter {
    private const val LESS_THAN_MINUTE = 1000 * 60
    private const val LESS_THAN_HOUR = LESS_THAN_MINUTE * 60

    fun format(context: Context, time: Date): String {
        val diff = System.currentTimeMillis() - time.time

        return when {
            diff < LESS_THAN_MINUTE -> {
                val seconds = diff.toInt() / 1000
                context.resources.getQuantityString(R.plurals.seconds, Math.abs(seconds), Math.abs(seconds))
            }
            diff < LESS_THAN_HOUR -> {
                val minutes = diff.toInt() / (1000 * 60)
                context.resources.getQuantityString(R.plurals.minutes, Math.abs(minutes), Math.abs(minutes))
            }
            else -> {
                val hours = diff.toInt() / (1000 * 60 * 60)
                context.resources.getQuantityString(R.plurals.hours, Math.abs(hours), Math.abs(hours))
            }
        }
    }
}