package com.harper.carnet.data.storage

/**
 * Created by HarperJr on 15:45
 **/
class SettingsStorage(private val sharedStorage: SharedStorage) {

    fun getAdapterHost(): String = sharedStorage.get(KEY_DEVICE_HOST, String::class)

    fun setAdapterHost(host: String) = sharedStorage.set(KEY_DEVICE_HOST, host)

    fun getAdapterPort(): Int = sharedStorage.get(KEY_DEVICE_PORT, Int::class)

    fun setAdapterPort(port: Int) = sharedStorage.set(KEY_DEVICE_PORT, port)

    companion object {
        private const val KEY_DEVICE_HOST = "KEY_DEVICE_HOST"
        private const val KEY_DEVICE_PORT = "KEY_DEVICE_PORT"
    }
}