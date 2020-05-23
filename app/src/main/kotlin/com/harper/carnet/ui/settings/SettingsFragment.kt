package com.harper.carnet.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import com.harper.carnet.ui.map.MapFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        caseNotifications.setOnClickListener { switchNotifications.isChecked = !switchNotifications.isChecked }
        caseNotificationsSound.setOnClickListener { switchNotificationsSound.isChecked = !switchNotificationsSound.isChecked }
        caseOfflineMap.setOnClickListener { onOfflineMapCaseClicked() }
    }

    private fun onOfflineMapCaseClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.action_settingsFragment_to_regionsFragment)
    }
}