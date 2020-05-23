package com.harper.carnet.data.storage

class AppStorage(private val sharedStorage: SharedStorage) {

    fun isIntroScreenShown(): Boolean = sharedStorage.get(KEY_INTRO_SCREEN_SHOWN, Boolean::class)

    fun setIntroScreenShown(value: Boolean) = sharedStorage.set(KEY_INTRO_SCREEN_SHOWN, value)

    companion object {
        private const val KEY_INTRO_SCREEN_SHOWN = "KEY_INTRO_SCREEN_SHOWN"
    }
}