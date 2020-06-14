package com.harper.carnet.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import com.harper.carnet.ext.observe
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            notificationsCheckLiveData.observe(this@SettingsFragment) { switchNotifications.isChecked = it }
            notificationSoundCheckLiveData.observe(this@SettingsFragment) { switchNotificationsSound.isChecked = it }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val shouldNavigateToConnectionCase = arguments?.getBoolean(ARG_CONNECTION_SETTINGS_CASE) ?: false
        if (shouldNavigateToConnectionCase)
            navigateToConnectionCase()

        caseNotifications.setOnClickListener { switchNotifications.isChecked = !switchNotifications.isChecked }
        caseNotificationsSound.setOnClickListener {
            switchNotificationsSound.isChecked = !switchNotificationsSound.isChecked
        }
        caseOfflineMap.setOnClickListener { onOfflineMapCaseClicked() }
        caseConnection.setOnClickListener { onConnectionCaseClicked() }
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onNotificationCaseSwitched(isChecked)
        }
        switchNotificationsSound.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onNotificationSoundCaseSwitched(isChecked)
        }
    }

    private fun onConnectionCaseClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.action_settingsFragment_to_connectionFragment)
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