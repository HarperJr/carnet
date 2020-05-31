package com.harper.carnet.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.TypeConverters
import com.harper.carnet.data.database.Table
import com.harper.carnet.data.database.entity.base.BaseEntity
import com.harper.carnet.data.database.entity.converter.SessionValueEntityConverter

@Entity(
    tableName = Table.SESSION_VALUES_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = CASCADE
        )
    ]
)
@TypeConverters(SessionValueEntityConverter::class)
class SessionValueEntity(
    val type: SessionValueTypeEntity,
    val sessionId: Int,
    val value: String
) : BaseEntity()