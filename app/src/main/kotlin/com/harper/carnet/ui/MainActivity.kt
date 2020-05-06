package com.harper.carnet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
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

        if (!appStorage.getShouldShowIntro()) {
            navController.navigate(R.id.introFragment)
        }
    }
}