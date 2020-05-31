package com.harper.carnet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.harper.carnet.R
import com.harper.carnet.data.storage.AppStorage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val appStorage: AppStorage by inject()
    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (appStorage.isIntroScreenShown()) {
            navController.navigate(R.id.mainFragment)
        } else navController.navigate(R.id.introFragment)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.fragments.find { it.isVisible }
        if (currentFragment != null) {
            val navController = currentFragment.findNavController()
            if (navController.popBackStack()) return
        }

        super.onBackPressed()
    }
}