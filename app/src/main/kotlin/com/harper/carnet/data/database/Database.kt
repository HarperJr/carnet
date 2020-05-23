package com.harper.carnet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harper.carnet.BuildConfig
import com.harper.carnet.data.database.dao.RegionsDao
import com.harper.carnet.data.database.dao.SessionsDao
import com.harper.carnet.data.database.entity.NotificationEntity
import com.harper.carnet.data.database.entity.RegionEntity
import com.harper.carnet.data.database.entity.SessionEntity
import com.harper.carnet.data.database.entity.SessionValueEntity

@Database(
    version = BuildConfig.DATABASE_VERSION,
    entities = [
        RegionEntity::class,
        SessionEntity::class,
        SessionValueEntity::class,
        NotificationEntity::class
    ]
)
abstract class Database : RoomDatabase() {

    abstract fun regionsDao(): RegionsDao

    abstract fun sessionsDao(): SessionsDao
}