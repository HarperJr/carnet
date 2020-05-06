package com.harper.carnet.ui.car

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.ui.car.adapter.WarningsAdapter
import kotlinx.android.synthetic.main.fragment_car.*

class CarFragment : Fragment(R.layout.fragment_car) {
    private val adapter: WarningsAdapter = WarningsAdapter { requireContext() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        warningsRecycler.adapter = adapter
    }
}