package com.harper.carnet.ui.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.harper.carnet.R
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.ui.intro.adapter.IntroAdapter
import com.harper.carnet.ui.intro.adapter.IntroViewItem
import kotlinx.android.synthetic.main.fragment_intro.*
import org.koin.android.ext.android.inject

class IntroFragment : Fragment(R.layout.fragment_intro) {
    private val appStorage: AppStorage by inject()
    private val navController: NavController by lazy {
        findNavController()
    }

    private var pagePosition: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnSkip.setOnClickListener { completeIntro() }
        with(viewPager) {
            adapter = IntroAdapter(requireContext(), ITEMS)
            currentItem = savedInstanceState?.getInt(STATE_PAGE_POS) ?: 0
        }
    }

    override fun onStart() {
        super.onStart()
        viewPager.registerOnPageChangeCallback(onPageChangedCallback)
    }

    override fun onStop() {
        viewPager.unregisterOnPageChangeCallback(onPageChangedCallback)
        super.onStop()
    }

    private fun completeIntro() {
        appStorage.setIntroScreenShown(true)
        navController.navigate(R.id.mainFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(STATE_PAGE_POS, pagePosition)
        })
    }

    private val onPageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            pagePosition = position
            if (pagePosition >= viewPager.childCount - 1) {
                completeIntro()
            }
        }
    }

    companion object {
        private const val STATE_PAGE_POS = "STATE_PAGE_POS"
        private val ITEMS = listOf(
            IntroViewItem(R.string.intro_first_title, R.string.intro_first_text),
            IntroViewItem(R.string.intro_second_title, R.string.intro_second_text),
            IntroViewItem(R.string.intro_third_title, R.string.intro_third_text)
        )
    }
}