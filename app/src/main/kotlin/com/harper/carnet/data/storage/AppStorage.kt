package com.harper.carnet.data.storage

class AppStorage(private val sharedStorage: SharedStorage) {

    fun getShouldShowIntro(): Boolean = sharedStorage.get(KEY_SHOULD_SHOW_INTRO, Boolean::class)

    fun setShouldShowIntro(value: Boolean) = sharedStorage.set(KEY_SHOULD_SHOW_INTRO, value)

    companion object {
        private const val KEY_SHOULD_SHOW_INTRO = "KEY_SHOULD_SHOW_INTRO"
    }
}