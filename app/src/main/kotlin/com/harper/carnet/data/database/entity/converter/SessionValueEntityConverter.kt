package com.harper.carnet.data.database.entity.converter

import androidx.room.TypeConverter
import com.harper.carnet.data.database.entity.SessionValueTypeEntity

/**
 * Created by HarperJr on 14:25
 **/
open class SessionValueEntityConverter {

    @TypeConverter
    fun fromOrdinal(ordinal: Int) = SessionValueTypeEntity.of(ordinal)

    @TypeConverter
    fun toOrdinal(entity: SessionValueTypeEntity) = entity.ordinal
}