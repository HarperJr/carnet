package com.harper.carnet.ui.settings.connection

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.ext.observe
import kotlinx.android.synthetic.main.fragment_connection_settings.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

/**
 * Created by HarperJr on 13:07
 **/
class ConnectionSettingsFragment : Fragment(R.layout.fragment_connection_settings) {
    private val viewModel: ConnectionSettingsViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            deviceIdentityLiveData.observe(this@ConnectionSettingsFragment) { deviceIdentityInput.setText(it) }
            errorLiveData.observe(this@ConnectionSettingsFragment, ::setError)
            connectionLiveData.observe(this@ConnectionSettingsFragment, ::setIsConnected)
            progressLiveData.observe(this@ConnectionSettingsFragment, ::setIsInProgress)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnConnect.setOnClickListener { viewModel.connect(deviceIdentityInput.text.toString()) }
    }

    private fun setError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setIsConnected(isConnected: Boolean) {
        connectionStatusText.setText(if (isConnected) R.string.connected else R.string.disconnected)
        connectionStatusIcon.setImageResource(if (isConnected) R.drawable.ic_check else R.drawable.ic_close)
    }

    private fun setIsInProgress(isInProgress: Boolean) {
        connectionProgress.visibility = if (isInProgress) View.VISIBLE else View.GONE
        btnConnect.visibility = if (isInProgress) View.INVISIBLE else View.VISIBLE
        btnConnect.isEnabled = !isInProgress
    }
}