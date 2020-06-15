package com.harper.carnet.ui.diagnostics

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.diagnostics.adapter.WarningsAdapter
import com.harper.carnet.ui.settings.SettingsFragment
import com.harper.carnet.ui.support.ValueFormatter
import kotlinx.android.synthetic.main.fragment_diagnostics.*
import kotlinx.android.synthetic.main.fragment_diagnostics.view.*
import kotlinx.android.synthetic.main.include_item_connection.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DiagnosticsFragment : Fragment(R.layout.fragment_diagnostics) {
    private val viewModel: DiagnosticsViewModel by currentScope.viewModel(this)
    private val timeHandler: TimeHandler = TimeHandler { onTimeElapsed(it) }
    private val adapter: WarningsAdapter = WarningsAdapter { requireContext() }

    private val dateFormatter: DateFormat = SimpleDateFormat("E d MMM yyyy", Locale.ENGLISH)
    private val timeFormatter: DateFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)
    private val dateTimeFormatter: DateFormat = SimpleDateFormat("dd.MM.yy hh:mm", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            diagnosticsLiveData.observe(this@DiagnosticsFragment, ::setValues)
            warningMessageLiveData.observe(this@DiagnosticsFragment, ::setWarnings)
            connectionLiveData.observe(this@DiagnosticsFragment, ::setIsConnected)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationIcon(R.drawable.ic_chart)
        toolbar.setNavigationOnClickListener { onChartsMenuItemClicked() }
        btnConfigureConnection.setOnClickListener { onConfigureConnectionBtnClicked() }
        with(warningsRecycler) {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            adapter = this@DiagnosticsFragment.adapter
        }
    }

    override fun onStart() {
        super.onStart()
        timeHandler.onStart()
    }

    override fun onStop() {
        timeHandler.onStop()
        super.onStop()
    }

    private fun setIsConnected(isConnected: Boolean) {
        connectionStatusIcon.setImageResource(if (isConnected) R.drawable.ic_network_active else R.drawable.ic_network_inactive)
        connectionStatusText.setText(if (isConnected) R.string.connection_status_active else R.string.connection_status_inactive)
        if (isConnected) {
            lastConnection.text = getString(R.string.last_connection, dateTimeFormatter.format(Date()))
            btnConfigureConnection.visibility = View.GONE
            lastConnection.visibility = View.VISIBLE
        } else {
            btnConfigureConnection.visibility = View.VISIBLE
            lastConnection.visibility = View.GONE
        }
    }

    private fun onConfigureConnectionBtnClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.settingsNavigation, SettingsFragment.argConnectionSettingsCase())
    }

    private fun setValues(diagnosticValues: List<DiagnosticValue<*>>) {
        if (view != null) {
            for ((valueType, viewId) in VALUE_IDS) {
                val value = diagnosticValues.find { it.type == valueType }
                if (viewId != -1)
                    requireView().findViewById<TextView>(viewId).text =
                        value?.let { ValueFormatter.format(it) } ?: getString(R.string.diagnostics_stub)
            }
        }
    }

    private fun onChartsMenuItemClicked() {
        Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
            .navigate(R.id.action_carFragment_to_chartsFragment)
    }

    private fun setWarnings(items: List<Warning>) {
        adapter.setItems(items)
    }

    private fun resolveValueId(diagnosticValue: DiagnosticValue<*>): Int {
        return VALUE_IDS[diagnosticValue.type] ?: -1
    }

    private fun onTimeElapsed(date: Date) {
        toolbar.date.text = dateFormatter.format(date)
        toolbar.time.text = timeFormatter.format(date)
    }

    companion object {
        private val VALUE_IDS = mapOf(
            ValueType.VOLTAGE to R.id.valueAccumulator,
            ValueType.ENGINE_TEMPERATURE to R.id.valueEngine,
            ValueType.TIME_RUN to R.id.valueTime,
            ValueType.SPEED to R.id.valueSpeed,
            ValueType.FUEL_LEVEL to R.id.valueFuel
        )
    }
}