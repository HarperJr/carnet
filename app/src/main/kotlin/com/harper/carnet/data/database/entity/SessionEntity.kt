package com.harper.carnet.data.database.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import com.harper.carnet.data.database.Tables
import com.harper.carnet.data.database.entity.base.BaseEntity
import com.harper.carnet.data.database.entity.converter.DateConverter
import com.harper.carnet.data.database.entity.converter.LatLngEntityConverter
import java.util.*

/**
 * Created by HarperJr on 13:03
 **/
@Entity(tableName = Tables.SESSIONS_TABLE)
@TypeConverters(DateConverter::class, LatLngEntityConverter::class)
class SessionEntity(
    val dateStart: Date,
    val dateEnd: Date,
    val startLocation: String,
    val endLocation: String,
    val routePath: List<LatLngEntity>
) : BaseEntity()