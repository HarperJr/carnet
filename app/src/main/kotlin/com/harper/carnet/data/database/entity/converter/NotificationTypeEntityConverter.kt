package com.harper.carnet.data.database.entity.converter

import androidx.room.TypeConverter
import com.harper.carnet.data.database.entity.NotificationTypeEntity

/**
 * Created by HarperJr on 14:24
 **/
open class NotificationTypeEntityConverter {

    @TypeConverter
    fun fromOrdinal(ordinal: Int) = NotificationTypeEntity.of(ordinal)

    @TypeConverter
    fun toOrdinal(entity: NotificationTypeEntity) = entity.ordinal
}