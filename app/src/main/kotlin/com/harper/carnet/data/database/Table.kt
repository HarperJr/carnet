package com.harper.carnet.data.database

import com.harper.carnet.data.database.dao.RegionsDao
import com.harper.carnet.data.database.dao.SessionsDao
import kotlin.reflect.KClass

enum class Table(val tableName: String, val daoClass: KClass<*>) {
    REGIONS(Table.REGIONS_TABLE, RegionsDao::class),
    SESSIONS(Table.SESSIONS_TABLE, SessionsDao::class);

    companion object {
        const val SESSION_VALUES_TABLE = "Values"
        const val REGIONS_TABLE = "Regions"
        const val SESSIONS_TABLE = "Sessions"
        const val NOTIFICATIONS_TABLE = "Notifications"

        fun fromDao(daoClass: KClass<*>) = values().find { it.daoClass == daoClass }
            ?: throw IllegalArgumentException("Unable to fine table by dao class=$daoClass")
    }
}