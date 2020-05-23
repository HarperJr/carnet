package com.harper.carnet.data.database.entity.converter

import androidx.room.TypeConverter
import com.harper.carnet.data.database.entity.LatLngEntity
import com.harper.carnet.data.gson.GSON

open class LatLngEntityConverter {

    @TypeConverter
    fun fromJson(string: String): LatLngEntity = GSON.fromJson(string)

    @TypeConverter
    fun toJson(entity: LatLngEntity): String = GSON.toJson(entity)

    @TypeConverter
    fun fromJsonList(string: String): List<LatLngEntity> = GSON.fromJson(string)

    @TypeConverter
    fun toJsonList(entities: List<LatLngEntity>): String = GSON.toJson(entities)
}