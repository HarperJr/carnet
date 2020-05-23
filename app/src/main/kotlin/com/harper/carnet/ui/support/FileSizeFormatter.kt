package com.harper.carnet.ui.support

import java.text.DecimalFormat

object FileSizeFormatter {
    private val formatters: List<DecimalFormat> =
        listOf(DecimalFormat("# Bytes"), DecimalFormat("# Kbytes"), DecimalFormat("#.# Mbytes"))

    fun format(size: Int): String {
        var formatterIndex = 0
        var adjustedSize = size.toDouble()
        while (adjustedSize / 1024.0 >= 1.0) {
            adjustedSize /= 1024.0
            formatterIndex++
        }

        return formatters.getOrNull(formatterIndex)?.format(adjustedSize)
            ?: throw IllegalStateException("Unable to find fit formatter")
    }
}