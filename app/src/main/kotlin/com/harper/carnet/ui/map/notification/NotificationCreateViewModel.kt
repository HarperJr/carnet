package com.harper.carnet.ui.map.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.map.NotificationSender
import com.harper.carnet.domain.map.location.LocationProvider
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.NotificationType
import com.harper.carnet.ext.rxLiveData
import com.harper.carnet.ui.map.NotificationViewType

class NotificationCreateViewModel(
    private val notificationSender: NotificationSender,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val locationsLiveData: LiveData<LatLng> = rxLiveData { locationProvider.updates() }
    private var notificationType: NotificationType? = null

    fun onNotificationSendBtnClicked(message: String) {
        val lastLocation = locationsLiveData.value
        if (lastLocation != null && notificationType != null) {
            notificationSender.sendNotification(notificationType!!, message, lastLocation.lat, lastLocation.lng)
        } else {
            //TODO handle this
        }
        notificationType = null
    }

    fun onNotificationTypeSelected(notificationViewType: NotificationViewType) {
        this.notificationType = getNotificationTypeFromViewType(notificationViewType)
    }

    private fun getNotificationTypeFromViewType(notificationViewType: NotificationViewType): NotificationType {
        return when (notificationViewType) {
            NotificationViewType.COLLAPSE -> NotificationType.COLLAPSE
            NotificationViewType.AMBULANCE -> NotificationType.AMBULANCE
            NotificationViewType.SUPPORT -> NotificationType.SUPPORT
            NotificationViewType.MESSAGE -> NotificationType.MESSAGE
        }
    }
}
