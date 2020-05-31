package com.harper.carnet.ui.car.charts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.harper.carnet.R
import kotlinx.android.synthetic.main.fragment_charts.*

class ChartsFragment : Fragment(R.layout.fragment_charts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment)
                .popBackStack()
        }
    }
}