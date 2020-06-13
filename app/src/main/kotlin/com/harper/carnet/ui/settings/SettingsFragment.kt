package com.harper.carnet.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val shouldNavigateToConnectionCase = arguments?.getBoolean(ARG_CONNECTION_SETTINGS_CASE) ?: false
        if (shouldNavigateToConnectionCase)
            navigateToConnectionCase()

        caseNotifications.setOnClickListener { switchNotifications.isChecked = !switchNotifications.isChecked }
        caseNotificationsSound.setOnClickListener {
            switchNotificationsSound.isChecked = !switchNotificationsSound.isChecked
        }
        caseOfflineMap.setOnClickListener { onOfflineMapCaseClicked() }
    }

    private fun onOfflineMapCaseClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.action_settingsFragment_to_regionsFragment)
    }

    private fun navigateToConnectionCase() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.action_settingsFragment_to_connectionFragment)
    }

    companion object {
        private const val ARG_CONNECTION_SETTINGS_CASE = "ARG_CONNECTION_SETTINGS_CASE"

        fun argConnectionSettingsCase(): Bundle = bundleOf(ARG_CONNECTION_SETTINGS_CASE to true)
    }
}