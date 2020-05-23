package com.harper.carnet.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.TypeConverters
import com.harper.carnet.data.database.Tables
import com.harper.carnet.data.database.entity.base.BaseEntity
import com.harper.carnet.data.database.entity.converter.DateConverter
import com.harper.carnet.data.database.entity.converter.LatLngEntityConverter
import com.harper.carnet.data.database.entity.converter.NotificationTypeEntityConverter
import java.util.*

@Entity(
    tableName = Tables.NOTIFICATIONS_TABLE,
    foreignKeys = [ForeignKey(
        entity = SessionEntity::class,
        parentColumns = ["id"],
        childColumns = ["sessionId"],
        onDelete = CASCADE
    )]
)
@TypeConverters(DateConverter::class, LatLngEntityConverter::class, NotificationTypeEntityConverter::class)
class NotificationEntity(
    val time: Date,
    val sessionId: Int,
    val type: NotificationTypeEntity,
    val location: LatLngEntity,
    val message: String
) : BaseEntity()