package com.harper.carnet.ui.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/**
 * Created by HarperJr on 21:40
 **/
class PushService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d("Message is received message=$message")
    }
}