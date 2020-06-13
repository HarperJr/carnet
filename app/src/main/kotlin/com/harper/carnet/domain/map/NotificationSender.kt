package com.harper.carnet.domain.map

import com.harper.carnet.data.api.ApiExecutor
import com.harper.carnet.domain.model.NotificationType
import io.reactivex.Completable

class NotificationSender(private val apiExecutor: ApiExecutor) {

    fun sendNotification(type: NotificationType, message: String, lat: Double, lng: Double): Completable {
        return apiExecutor.publishNotification(message, type.code, lat, lng)
    }
}
