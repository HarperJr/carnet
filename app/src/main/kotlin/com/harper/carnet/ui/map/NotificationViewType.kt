package com.harper.carnet.ui.map

import androidx.annotation.StringRes
import com.harper.carnet.R

/**
 * Created by HarperJr on 12:44
 **/
enum class NotificationViewType(@StringRes val titleRes: Int) {
    AMBULANCE(R.string.notification_ambulance),
    COLLAPSE(R.string.notification_collapse),
    SUPPORT(R.string.notification_support),
    MESSAGE(R.string.notification_message)
}