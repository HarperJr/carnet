package com.harper.carnet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.harper.carnet.R
import com.harper.carnet.data.storage.AppStorage
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val appStorage: AppStorage by inject()
    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (appStorage.getShouldShowIntro()) {
            navController.navigate(R.id.introFragment)
        } else navController.navigate(R.id.mainFragment)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var currentFragment: Fragment? = null
        for (fragment in navHostFragment.childFragmentManager.fragments) {
            if (fragment.isVisible) {
                currentFragment = fragment
                break
            }
        }

        currentFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}