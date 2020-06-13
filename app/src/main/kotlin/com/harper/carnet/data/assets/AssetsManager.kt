package com.harper.carnet.data.assets

import android.content.Context
import java.io.InputStream

class AssetsManager(private val context: Context) {

    fun getFileAsInputStream(fileName: String): InputStream? {
        return kotlin.runCatching { context.assets.open(fileName) }
            .getOrNull()
    }
}