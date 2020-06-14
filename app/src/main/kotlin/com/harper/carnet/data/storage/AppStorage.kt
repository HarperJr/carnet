package com.harper.carnet.data.storage

class AppStorage(private val sharedStorage: SharedStorage) {

    fun isIntroScreenShown(): Boolean = sharedStorage.get(KEY_INTRO_SCREEN_SHOWN, Boolean::class)

    fun setIntroScreenShown(value: Boolean) = sharedStorage.set(KEY_INTRO_SCREEN_SHOWN, value)

    fun getJwtToken(): String = sharedStorage.get(KEY_JWT_TOKEN, String::class)

    fun setJwtToken(token: String) = sharedStorage.set(KEY_JWT_TOKEN, token)

    fun getDeviceIdentity(): String = sharedStorage.get(KEY_DEVICE_IDENTITY, String::class)

    fun setDeviceIdentity(identity: String) = sharedStorage.set(KEY_DEVICE_IDENTITY, identity)

    fun getPushToken() = sharedStorage.get(KEY_PUSH_TOKEN, String::class)

    fun setPushToken(token: String) = sharedStorage.set(KEY_PUSH_TOKEN, token)

    companion object {
        private const val KEY_INTRO_SCREEN_SHOWN = "KEY_INTRO_SCREEN_SHOWN"
        private const val KEY_DEVICE_IDENTITY = "KEY_DEVICE_IDENTITY"
        private const val KEY_PUSH_TOKEN = "KEY_PUSH_TOKEN"
        private const val KEY_JWT_TOKEN = "KEY_JWT_TOKEN"
    }
}