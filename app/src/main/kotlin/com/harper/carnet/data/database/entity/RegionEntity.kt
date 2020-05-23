package com.harper.carnet.data.database.entity

import androidx.room.Entity
import com.harper.carnet.data.database.Tables
import com.harper.carnet.data.database.entity.base.BaseEntity

@Entity(tableName = Tables.REGIONS_TABLE)
class RegionEntity(
    val code: String,
    val name: String,
    val order: Long,
    val north: Double,
    val south: Double,
    val east: Double,
    val west: Double
) : BaseEntity()