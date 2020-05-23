package com.harper.carnet.data.database.entity.base

import androidx.room.PrimaryKey

open class BaseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)