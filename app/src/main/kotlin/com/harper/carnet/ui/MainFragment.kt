package com.harper.carnet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.harper.carnet.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomNavView.setupWithNavController(
            Navigation.findNavController(
                requireActivity(),
                R.id.nestedNavHostFragment
            )
        )
    }

    fun onBackPressed(): Boolean {
        return Navigation.findNavController(requireActivity(), R.id.nestedNavHostFragment).popBackStack()
    }
}