package com.harper.carnet.data.database.dao.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.harper.carnet.data.database.entity.base.BaseEntity

interface BaseDao<T : BaseEntity> {

    @Insert
    fun insert(entity: T)

    @Insert
    fun insert(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @RawQuery
    fun drop(query: SupportSQLiteQuery): Boolean

    @RawQuery
    fun delete(query: SupportSQLiteQuery): Boolean

    @RawQuery
    fun find(query: SupportSQLiteQuery): T

    @RawQuery
    fun findAll(query: SupportSQLiteQuery): List<T>

    @Update
    fun update(entity: T)
}