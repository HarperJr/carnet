package com.harper.carnet.ui.car

import android.os.Handler
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class TimeHandler(private val callback: (Date) -> Unit) {
    private val isRunning: AtomicBoolean = AtomicBoolean(false)
    private var handler: Handler = Handler()

    fun onStart() {
        isRunning.set(true)
        elapse()
    }

    fun onStop() {
        isRunning.set(false)
        handler.removeCallbacks(runnable)
    }

    private fun elapse() {
        if (isRunning.get()) {
            handler.postDelayed(runnable, DELAY)
            callback.invoke(Date())
        }
    }

    private val runnable: Runnable = Runnable {
        handler.postDelayed({ elapse() }, DELAY)
    }

    companion object {
        private const val DELAY = 60000L
    }
}