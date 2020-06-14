package com.harper.carnet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.harper.carnet.R
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.ui.map.TelematicsService
import com.harper.carnet.ui.settings.regions.service.RegionLoadingService
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun onStart() {
        super.onStart()
        startService(Intent(this, TelematicsService::class.java))
    }

    override fun onBackPressed() {
        val currentFragment = navHostFragment.childFragmentManager.fragments.find { it.isVisible }
        if (currentFragment != null && currentFragment is MainFragment)
            if (currentFragment.onBackPressed()) return

        super.onBackPressed()
    }
}