package com.harper.carnet.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.harper.carnet.data.database.Table
import com.harper.carnet.data.database.dao.base.BaseDao
import com.harper.carnet.data.database.entity.SessionEntity

/**
 * Created by HarperJr on 13:03
 **/
@Dao
interface SessionsDao : BaseDao<SessionEntity> {

    @Query("SELECT * FROM Sessions WHERE isActive = 1 LIMIT 1")
    fun findActiveSession(): SessionEntity?

    @Query("SELECT * FROM Sessions WHERE isActive = 0")
    fun fundNonActiveSessions(): List<SessionEntity>
}