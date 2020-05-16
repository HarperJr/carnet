package com.harper.carnet.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        caseNotifications.setOnClickListener { switchNotifications.isChecked = !switchNotifications.isChecked }
        caseNotificationsSound.setOnClickListener { switchNotificationsSound.isChecked = !switchNotificationsSound.isChecked }
    }
}