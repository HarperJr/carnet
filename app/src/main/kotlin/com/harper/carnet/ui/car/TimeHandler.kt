package com.harper.carnet.ui.car

import android.os.Handler
import java.util.*

class TimeHandler(private val callback: (Date) -> Unit) {
    private var handler: Handler = Handler()

    fun onStart() {
        elapse()
    }

    fun onStop() {
        handler.removeCallbacks(runnable)
    }

    private fun elapse() {
        handler.postDelayed(runnable, DELAY)
        callback.invoke(Date())
    }

    private val runnable: Runnable = Runnable {
        handler.postDelayed({ elapse() }, DELAY)
    }

    companion object {
        private const val DELAY = 60000L
    }
}