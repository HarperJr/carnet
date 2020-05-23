package com.harper.carnet.data.database

import com.harper.carnet.data.database.dao.RegionsDao
import com.harper.carnet.data.database.dao.ValuesDao
import kotlin.reflect.KClass

enum class Tables(val tableName: String, val daoClass: KClass<*>) {
    VALUES(Tables.VALUES_TABLE, ValuesDao::class),
    REGIONS(Tables.VALUES_TABLE, RegionsDao::class);

    companion object {
        const val VALUES_TABLE = "Values"
        const val REGIONS_TABLE = "Regions"
    }
}