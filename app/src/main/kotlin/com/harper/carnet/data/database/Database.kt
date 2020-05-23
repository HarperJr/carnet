package com.harper.carnet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harper.carnet.BuildConfig
import com.harper.carnet.data.database.dao.RegionsDao
import com.harper.carnet.data.database.dao.ValuesDao
import com.harper.carnet.data.database.entity.RegionEntity
import com.harper.carnet.data.database.entity.ValueEntity

@Database(
    version = BuildConfig.DATABASE_VERSION,
    entities = [
        RegionEntity::class,
        ValueEntity::class
    ]
)
abstract class Database : RoomDatabase() {

    abstract fun regionsDao(): RegionsDao

    abstract fun valuesDao(): ValuesDao
}