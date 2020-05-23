package com.harper.carnet.data.database.entity

import androidx.room.Entity
import com.harper.carnet.data.database.Tables
import com.harper.carnet.data.database.entity.base.BaseEntity

@Entity(tableName = Tables.VALUES_TABLE)
class ValueEntity(
    val voltage: Double,
    val speed: Int,
    val engine: Int,
    val rpm: Int,
    val fuel: Int
) : BaseEntity()