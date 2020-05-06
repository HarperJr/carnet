package com.harper.carnet.ui.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harper.carnet.R
import com.harper.carnet.ui.intro.adapter.IntroAdapter
import com.harper.carnet.ui.intro.adapter.IntroViewItem
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : Fragment(R.layout.fragment_intro) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (viewPager) {
            adapter = IntroAdapter(requireContext(), ITEMS)
            currentItem = savedInstanceState?.getInt(STATE_PAGE_POS) ?: 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(STATE_PAGE_POS, viewPager.currentItem)
        })
    }

    companion object {
        private const val STATE_PAGE_POS = "STATE_PAGE_POS"
        private val ITEMS = listOf<IntroViewItem>()
    }
}