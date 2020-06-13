package com.harper.carnet.ui.map.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.ui.map.NotificationViewType
import kotlinx.android.synthetic.main.fragment_notification_create.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

/**
 * Created by HarperJr on 14:21
 **/
class NotificationCreateFragment : Fragment(R.layout.fragment_notification_create) {
    private val createViewModel: NotificationCreateViewModel by currentScope.viewModel(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notificationAmbulance.setOnClickListener { onNotificationTypeSelected(it) }
        notificationCollapse.setOnClickListener { onNotificationTypeSelected(it) }
        notificationSupport.setOnClickListener { onNotificationTypeSelected(it) }
        notificationMessage.setOnClickListener { onNotificationTypeSelected(it) }

        btnNotificationSend.setOnClickListener {
            createViewModel.onNotificationSendBtnClicked(
                notificationInput.text.toString()
            )
        }
    }

    private fun onNotificationTypeSelected(view: View) {
        val notificationViewType = NOTIFICATION_VIEW_TYPES[view.id]
        notificationViewType?.let { viewType ->
            notificationLabel.setText(viewType.titleRes)
            createViewModel.onNotificationTypeSelected(viewType)
        }
    }

    companion object {
        private val NOTIFICATION_VIEW_TYPES = mapOf(
            R.id.notificationAmbulance to NotificationViewType.AMBULANCE,
            R.id.notificationCollapse to NotificationViewType.COLLAPSE,
            R.id.notificationSupport to NotificationViewType.SUPPORT,
            R.id.notificationMessage to NotificationViewType.MESSAGE
        )
    }
}