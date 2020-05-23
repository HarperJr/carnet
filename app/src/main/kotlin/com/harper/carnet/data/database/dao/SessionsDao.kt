package com.harper.carnet.data.database.dao

import androidx.room.Dao
import com.harper.carnet.data.database.dao.base.BaseDao
import com.harper.carnet.data.database.entity.SessionEntity

/**
 * Created by HarperJr on 13:03
 **/
@Dao
interface SessionsDao : BaseDao<SessionEntity>