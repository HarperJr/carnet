package com.harper.carnet.data.database.entity.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by HarperJr on 14:13
 **/
open class DateConverter {

    @TypeConverter
    fun toLong(date: Date?): Long = date?.time ?: 0L

    @TypeConverter
    fun toDate(time: Long): Date = Date(time)
}