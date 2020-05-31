package com.harper.carnet.data.database.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import com.harper.carnet.data.database.Table
import com.harper.carnet.data.database.entity.base.BaseEntity
import com.harper.carnet.data.database.entity.converter.DateConverter
import com.harper.carnet.data.database.entity.converter.LatLngEntityConverter
import java.util.*

/**
 * Created by HarperJr on 13:03
 **/
@Entity(tableName = Table.SESSIONS_TABLE)
@TypeConverters(DateConverter::class, LatLngEntityConverter::class)
class SessionEntity(
    val startTime: Date,
    val endTime: Date?,
    val isActive: Boolean,
    val startLocation: LatLngEntity,
    val endLocation: LatLngEntity,
    val startLocationName: String,
    val endLocationName: String,
    val routePath: List<LatLngEntity>
) : BaseEntity()