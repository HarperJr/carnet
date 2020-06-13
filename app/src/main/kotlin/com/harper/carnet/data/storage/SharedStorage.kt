package com.harper.carnet.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlin.reflect.KClass

class SharedStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(STORAGE_NAME, MODE_PRIVATE)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, type: KClass<T>): T {
        val value: Any = when (type) {
            Int::class -> sharedPreferences.getInt(key, 0)
            String::class -> sharedPreferences.getString(key, "")
            Boolean::class -> sharedPreferences.getBoolean(key, false)
            Long::class -> sharedPreferences.getLong(key, 0L)
            Float::class -> sharedPreferences.getFloat(key, 0f)
            else -> throw IllegalArgumentException("Unsupported type=$type")
        }

        return value as T
    }

    fun <T : Any> set(key: String, value: T) = with(sharedPreferences.edit()) {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> throw IllegalArgumentException("Unsupported type=${value::class}")
        }
    }.commit()

    companion object {
        private const val STORAGE_NAME = "SharedStorage"
    }
}
