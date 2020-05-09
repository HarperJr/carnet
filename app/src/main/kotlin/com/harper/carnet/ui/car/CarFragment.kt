package com.harper.carnet.ui.car

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Value
import com.harper.carnet.domain.model.ValueType
import com.harper.carnet.domain.model.Warning
import com.harper.carnet.ui.car.adapter.WarningsAdapter
import com.harper.carnet.ui.support.ValueFormatter
import kotlinx.android.synthetic.main.fragment_car.*
import kotlinx.android.synthetic.main.fragment_car.view.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CarFragment : Fragment(R.layout.fragment_car) {
    private val adapter: WarningsAdapter = WarningsAdapter { requireContext() }
    private val viewModel: CarViewModel by currentScope.viewModel(this)
    private val timeHandler: TimeHandler = TimeHandler { onTimeElapsed(it) }

    private val dateFormatter: DateFormat = SimpleDateFormat("E d MMM yyyy", Locale.getDefault())
    private val timeFormatter: DateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            valuesLiveData.observe({ lifecycle }, ::setValues)
            warningMessageLiveData.observe({ lifecycle }, ::setWarnings)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(warningsRecycler) {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            adapter = this@CarFragment.adapter
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

    private fun setValues(values: List<Value<*>>) {
        if (view != null) {
            for (value in values)
                view!!.findViewById<TextView>(resolveValueId(value)).text = ValueFormatter.format(value)
        }
    }

    private fun setWarnings(items: List<Warning>) {
        adapter.setItems(items)
    }

    private fun resolveValueId(value: Value<*>): Int {
        return VALUE_IDS[value.type]
            ?: throw IllegalArgumentException("Unable to resolve value id by type=${value.type}")
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